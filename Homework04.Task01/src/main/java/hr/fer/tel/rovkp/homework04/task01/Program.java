/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework04.task01;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author aelek
 */
public class Program {
    private static String pathToFiles = "./src/main/resources/";
    private static String ignoreFilename = "sensorscope-monitor-def.txt";
    private static String outPath = "./target/senesorscope-monitor-all.csv.";
    
    public static void main(String[] args) throws IOException {
        List<File> files = getFiles(pathToFiles, ignoreFilename);
        System.out.println(files.size() + " input files.");
        
        // Concatenate streams
        Stream<String> lines1 = Files.lines(Paths.get(files.get(0).getPath()));
        for(int i = 1; i < files.size(); i++) {
            Stream<String> lines2 = Files.lines(Paths.get(files.get(i).getPath()));
            lines1 = Stream.concat(lines1, lines2);
        }
        
        // Filter and convert streams
        SensorscopeReading reading = new SensorscopeReading();
        Stream<SensorscopeReading> readings = lines1
                .filter(l -> reading.Parse(l))
                .map(l -> new SensorscopeReading(l))
                .sorted();
        
        // Write to file
         try(PrintWriter writer = new PrintWriter(new FileWriter(outPath))){
             readings.forEach(r -> {
                 writer.println(r.toString());
             });
         }
        
    }
    
    private static List<File> getFiles(String path, String ignoreFilename){
        File folder = new File(path);
        File[] allFiles = folder.listFiles();
        List<File> validFiles = new ArrayList<>();
        
        for (File file : allFiles) {
            if (file.isFile() && !file.getName().equalsIgnoreCase(ignoreFilename)) {
                validFiles.add(file);
            }
        }
        return validFiles;
    }
}
