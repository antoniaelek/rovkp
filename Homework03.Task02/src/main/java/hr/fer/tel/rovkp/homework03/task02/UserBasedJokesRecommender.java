/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework03.task02;

import java.io.File;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;

/**
 *
 * @author aelek
 */
public class UserBasedJokesRecommender {
    public static void main(String[] args) throws Exception {
        DataModel model = new FileDataModel(new File(args[0]));
        GenericUserBasedRecommender recommender = RecommenderUtils.userBasedRecommender(model);
    }
}
