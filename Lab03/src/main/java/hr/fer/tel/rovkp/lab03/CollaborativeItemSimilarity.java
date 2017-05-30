package hr.fer.tel.rovkp.lab03;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

/**
 *
 * @author Krešimir Pripužić <kresimir.pripuzic@fer.hr>
 */
public class CollaborativeItemSimilarity implements ItemSimilarity {

    private final double[][] matrix;
    private final double[] norms;
    private final Map<Integer, Long> seqIdMap;
    private final Map<Long, Integer> idSeqMap;

    public CollaborativeItemSimilarity(DataModel model) throws TasteException {
        int n = model.getNumItems();
        matrix = new double[n][n];
        norms = new double[n];
        seqIdMap = new HashMap<>();
        idSeqMap = new HashMap<>();

        calculateCollaborativeModelMatrix(model);
    }

    private void calculateCollaborativeModelMatrix(DataModel model) throws TasteException {

        int counter = 0;
        LongPrimitiveIterator iterator = model.getUserIDs();
        while (iterator.hasNext()) {
            long userId = iterator.nextLong();

            for (long ratedItemId1 : model.getItemIDsFromUser(userId)) {

                //get correct item1 seq num
                Integer seqId1 = idSeqMap.get(ratedItemId1);
                if (seqId1 == null) {
                    seqId1 = counter++;
                    seqIdMap.put(seqId1, ratedItemId1);
                    idSeqMap.put(ratedItemId1, seqId1);
                }

                norms[seqId1] += Math.pow(model.getPreferenceValue(userId, ratedItemId1), 2);

                for (long ratedItemId2 : model.getItemIDsFromUser(userId)) {

                    //get correct item2 seq num
                    Integer seqId2 = idSeqMap.get(ratedItemId2);
                    if (seqId2 == null) {
                        seqId2 = counter++;
                        seqIdMap.put(seqId2, ratedItemId2);
                        idSeqMap.put(ratedItemId2, seqId2);
                    }

                    matrix[seqId1][seqId2] += model.getPreferenceValue(userId, ratedItemId1)
                            * model.getPreferenceValue(userId, ratedItemId2);
                }
            }
        }

	//get cosine similarity from similarity sums
        for (int seqId1 = 0; seqId1 < matrix.length; seqId1++) {
            for (int seqId2 = 0; seqId2 < matrix.length; seqId2++) {
                if (matrix[seqId1][seqId2] != 0) {                                       
                    matrix[seqId1][seqId2] /= Math.sqrt(norms[seqId1]) * Math.sqrt(norms[seqId2]);
                }
            }
        }
    }

    @Override
    public double itemSimilarity(long itemID1, long itemID2) throws TasteException {
        long[] items = new long[1];
        items[0] = itemID2;
        double[] result = itemSimilarities(itemID1, items);
        return result[0];
    }

    @Override
    public double[] itemSimilarities(long itemID1, long[] itemID2s) throws TasteException {
        double[] similarities = new double[itemID2s.length];
        int seqId1 = idSeqMap.get(itemID1);
        for (int i = 0; i < itemID2s.length; i++) {
            int seqIdCurr = idSeqMap.get(itemID2s[i]);
            similarities[i] = matrix[seqId1][seqIdCurr];
        }
        return similarities;
    }

    @Override
    public long[] allSimilarItemIDs(long itemID) throws TasteException {
        HashSet<Long> ids = new HashSet<>();
        int seqId1 = idSeqMap.get(itemID);
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[seqId1][i] > 0 && i != seqId1)
                ids.add(seqIdMap.get(i));
        }
        long[] idsArray = new long[ids.size()];
        int arrayIdx = 0;
        for(long id : ids) {
            idsArray[arrayIdx] = id;
            arrayIdx++;
        }
        return idsArray;
    }

    @Override
    public void refresh(Collection<Refreshable> alreadyRefreshed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
