/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework03.task02;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

/**
 *
 * @author aelek
 */
public class UserBasedJokesRecommender {
    public static void main(String[] args) throws Exception {
        String fileDataModel = "./src/main/resources/jester_ratings.dat";
        String users = "./src/main/resources/users.txt";
        String outFile = "./target/user_based_recommendations.txt";
        
        int CNT = 100;
        
        DataModel model = new FileDataModel(new File(fileDataModel));
        GenericUserBasedRecommender recommender = RecommenderUtils.userBasedRecommender(model);
        
        List<RecommendedItem> recommendations = recommender.recommend(220, 10);
        for (RecommendedItem recommendation : recommendations) {
            System.out.println(recommendation);
        }
        
        writeTopN(CNT, users, recommender, outFile);
        
    }

    private static void writeTopN(int n, String usersFile, 
            GenericUserBasedRecommender recommender, String outFile) 
            throws IOException {
        int[] cnt = new int[1];
        cnt[0] = 0;
        
        String[] ret = new String[n];
        
        try (Stream<String> stream = Files.lines(Paths.get(usersFile))) {
            stream.forEach(i -> {
                if (cnt[0] >= n) return;
                String retVal = printRecommendations(i,recommender);
                if (!"".equals(retVal)) {
                    ret[cnt[0]] = retVal;
                    cnt[0] += 1;
                }
            });
        }
        
        try(FileWriter fw = new FileWriter(outFile, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
        {
            for(String line : ret){
                out.println(line);
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    public static String printRecommendations (String id, GenericUserBasedRecommender recommender) {
        try {
            String ret = "";
            int i = Integer.parseInt(id);
            ret += "User " + i + "\n";
            List<RecommendedItem> recommendations = recommender.recommend(i, 10);
            for (RecommendedItem recommendation : recommendations) {
                ret += recommendation + "\n";
            }
            return ret;
        } catch (TasteException ex) {
            return "";
        }
    }
}
