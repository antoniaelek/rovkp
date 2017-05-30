/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.lab03;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.similarity.file.FileItemSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

/**
 *
 * @author aelek
 */
public class HybridMatrix {
    public static String hybridPath = "./target/hybrid_item_similarity.csv";
   
    public static void main(String[] args) throws IOException, TasteException {
        DataModel model = new FileDataModel(
                new File("./src/main/resources/jester_ratings.dat"), ",");

        ItemSimilarity lSim = new FileItemSimilarity(
                new File("./src/main/resources/item_similarity.csv"));
        
        CollaborativeItemSimilarity cSim = new CollaborativeItemSimilarity(model);
        
        double factor = 0.5;
        

        try(PrintWriter writer = new PrintWriter(new FileWriter(hybridPath))){
            for (int i = 1; i <= 150; i++){
                for(int j = i + 1; j <= 150; j++)
                    try {
                        double sim = factor * lSim.itemSimilarity(i, j) +
                                     (1 - factor) * cSim.itemSimilarity(i, j);
                        if (!Double.isNaN(sim)) 
                            writer.println(i + "," + j + "," + sim);
                    } catch (Exception ex) {}
            }
        }
    }
}
