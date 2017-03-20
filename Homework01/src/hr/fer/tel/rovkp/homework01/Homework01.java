/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework01;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aelek
 */
public class Homework01 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {       
        try {
            String from = "./gutenberg";
            String to = "./gutenberg/merged.txt";
            int count = 0;
            
            // Because heap, that's bloody why
            File file = new File(from);
            File[] directories = new File(from).listFiles(File::isDirectory);
            for(File dir : directories) {
                count += FileReader.mergeFiles(dir.getPath(), StandardCharsets.ISO_8859_1, to, StandardOpenOption.CREATE);
            }
            
            System.out.println("Total: " + count + " lines.");
            
        } catch (IOException ex) {
            Logger.getLogger(Homework01.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
