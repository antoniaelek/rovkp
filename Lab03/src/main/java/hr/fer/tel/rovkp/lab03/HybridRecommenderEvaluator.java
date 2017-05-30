/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.lab03;

import java.io.File;
import java.io.IOException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.file.FileItemSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

/**
 *
 * @author aelek
 */
public class HybridRecommenderEvaluator {
    public static String hybridPath = "./target/hybrid_item_similarity.csv";
    
    public static RecommenderBuilder hybridRecommender() {
        return (DataModel dataModel) -> {
            ItemSimilarity similarity = new FileItemSimilarity(new File(hybridPath));
            return new GenericItemBasedRecommender(dataModel, similarity);
        };
    }
    
    public static void main(String[] args) throws IOException, TasteException {
        DataModel model = new FileDataModel(
                new File("./src/main/resources/jester_ratings.dat"), ",");
        System.out.println(new RMSRecommenderEvaluator()
                .evaluate(hybridRecommender(), null, model,0.7, 1.0));
    }
}
