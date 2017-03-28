/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework01.task02;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;

/**
 *
 * @author aelek
 */
public class Program {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {       
        try {
            
            if (args.length < 2) {
                System.err.println("Usage: Program <from> <to>");
                System.exit(1);
            }
            
            String from = args[0]; // ./gutenberg
            String to = args[1]; // ./gutenberg/gutenberg_books.txt
            int count = 0;
            
//            count += FileReader.mergeFiles(from, to, 
//                    StandardCharsets.ISO_8859_1, StandardOpenOption.CREATE);
            
            File[] directories = new File(from).listFiles(File::isDirectory);
            for(File dir : directories) {
                count += FileReader.mergeFiles(dir.getPath(), to, 
                                               StandardCharsets.ISO_8859_1,
                                               StandardOpenOption.APPEND
                                              );
            }
            
            System.out.println("Total: " + count + " lines.");
            
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
}
