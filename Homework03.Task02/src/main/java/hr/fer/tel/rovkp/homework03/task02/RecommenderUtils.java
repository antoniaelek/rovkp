/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework03.task02;

import java.io.File;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.file.FileItemSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
 *
 * @author aelek
 */
public class RecommenderUtils {
    public static GenericUserBasedRecommender userBasedRecommender(DataModel model) throws TasteException { 
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        return new GenericUserBasedRecommender(model, neighborhood, similarity);
    }
    
    public static GenericItemBasedRecommender itemBasedRecommender(DataModel model, String fileItemsSimilarity) throws TasteException {
       ItemSimilarity similarity = new FileItemSimilarity(new File(fileItemsSimilarity));
        return new GenericItemBasedRecommender(model, similarity);
    }
}
