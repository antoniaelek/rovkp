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
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

/**
 *
 * @author aelek
 */
public class Program {
     public static void main(String[] args) throws Exception {
        String fileItemsSimilarity = "./src/main/resources/item_similarity.csv";
        String fileDataModel = "./src/main/resources/jester_ratings.dat";
        
        DataModel model = new FileDataModel(new File(fileDataModel));
        GenericUserBasedRecommender userBased = RecommenderUtils.userBasedRecommender(model);
        GenericItemBasedRecommender itemBased = RecommenderUtils.itemBasedRecommender(model, fileItemsSimilarity);
        
        System.out.println("user based:");        
        List<RecommendedItem> recommendationsU = userBased.recommend(220, 10);
        for (RecommendedItem recommendation : recommendationsU) {
            System.out.println(recommendation);
        }
            
        System.out.println("item based:");
        List<RecommendedItem> recommendationsI = itemBased.recommend(220, 10);
        for (RecommendedItem recommendation : recommendationsI) {
            System.out.println(recommendation);
        }
     }
}
