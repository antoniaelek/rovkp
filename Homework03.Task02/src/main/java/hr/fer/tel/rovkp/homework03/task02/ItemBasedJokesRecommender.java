/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework03.task02;

import java.io.File;
import java.util.List;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

/**
 *
 * @author aelek
 */
public class ItemBasedJokesRecommender {
    public static void main(String[] args) throws Exception {
        String fileItemsSimilarity = "./src/main/resources/item_similarity.csv";
        String fileDataModel = "./src/main/resources/jester_ratings.dat";
        
        DataModel model = new FileDataModel(new File(fileDataModel));
        GenericItemBasedRecommender recommender = RecommenderUtils.itemBasedRecommender(model, fileItemsSimilarity);
        
        List<RecommendedItem> recommendations = recommender.recommend(220, 10);
        for (RecommendedItem recommendation : recommendations) {
            System.out.println(recommendation);
        }

    }
}
