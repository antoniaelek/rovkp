/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework03.task02;

import java.io.File;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.common.RandomUtils;

/**
 *
 * @author aelek
 */
public class ItemBasedRecommenderEvaluator {
    public static void main(String[] args) throws Exception {

        RandomUtils.useTestSeed();

        String fileItemsSimilarity = "./src/main/resources/item_similarity.csv";
        String fileDataModel = "./src/main/resources/jester_ratings_small.dat";
        
        DataModel model = new FileDataModel(new File(fileDataModel));
        
        RecommenderBuilder builder = (DataModel m) -> 
                RecommenderUtils.itemBasedRecommender(m, fileItemsSimilarity);

        RecommenderEvaluator recEvaluator = new RMSRecommenderEvaluator();
        double score = recEvaluator.evaluate(builder, null, model, 0.7, 1.0);
        System.out.println(score);
    }
}
