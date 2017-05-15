/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework03.task02;

import java.io.File;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.file.FileItemSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

/**
 *
 * @author aelek
 */
public class ItemBasedJokesRecommender {
    public static void main(String[] args) throws Exception {
        String fileItemsSimilarity = "./src/main/resources/item_similarity.csv";
        String fileDataModel = "./src/main/resources/jester_ratings.dat";
        
        DataModel model = new FileDataModel(new File(fileDataModel));
        ItemSimilarity similarity = new FileItemSimilarity(new File(fileItemsSimilarity));
        GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, similarity);
    }
}
