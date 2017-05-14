/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework03.task01;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 *
 * @author aelek
 */
public class Program {
    public static void main(String[] args) throws ParseException {
        try {
            String path = "./src/main/resources/jester_items.dat";
            String outFile = "item_similarity.csv";
            
            Map<Integer, String> jokes = JokesCollection.parseInputFile(path);
            
            float[][] similarityMatrix = JokesCollection.createSimilarityMatrix(jokes);
            
            JokesCollection.similarityMatrixAsCsv(similarityMatrix,outFile);
            
        } catch (IOException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
