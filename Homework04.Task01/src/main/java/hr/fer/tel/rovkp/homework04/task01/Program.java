package hr.fer.tel.rovkp.homework04.task01;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Program {
    private static final String PATH_TO_FILES = "./src/main/resources/";
    private static final String IGNORE_FILENAME = "sensorscope-monitor-def.txt";
    private static final String OUT_PATH = "./target/senesorscope-monitor-all.csv";
    
    public static void main(String[] args) throws IOException {
        List<File> files = getFiles(PATH_TO_FILES, IGNORE_FILENAME);
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
         try(PrintWriter writer = new PrintWriter(new FileWriter(OUT_PATH))){
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
