/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.lab01;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

/**
 *
 * @author aelek
 */
public class Program {
    public static void main(String[] args) {       
        if (args.length < 2) {
            System.err.println("Usage: Program <from> <to>");
            System.exit(1);
        }
        
        FileReaderWriter frw = new FileReaderWriter();
        try {
            frw.work(args[0], args[1], StandardCharsets.ISO_8859_1);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        System.out.println("Read " + frw.readLinesCount() + " lines from  " + frw.readFilesCount() + " files.");
    }
    
    public static List<String> readFiles(String path, String hdfsURI, Charset charset) throws IOException, URISyntaxException{
        Configuration config = new Configuration();
        FileSystem hdfs = FileSystem.get(new URI(hdfsURI), config);
        
        Path rootDir = Paths.get(path);
        List<String> lines = new ArrayList<>();
        
        Files.walkFileTree(rootDir, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                try (Stream<String> stream = Files.lines(file, charset)) {
                    stream.forEach(line -> lines.add(line));
                }

                return FileVisitResult.CONTINUE;
            }
        });
        
        return lines;
    }
}
