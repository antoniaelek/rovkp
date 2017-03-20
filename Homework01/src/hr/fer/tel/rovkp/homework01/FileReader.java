/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework01;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author aelek
 */
public class FileReader {
    public static int mergeFiles(String from, Charset charset, String to, StandardOpenOption openOption) throws IOException{
        Path newFile = Paths.get(to);
        List<String> lines = readFiles(from, charset);
        Files.write(newFile, lines, charset, openOption);
        return lines.size();
    }
    
    public static List<String> readFiles(String path, Charset charset) throws IOException{
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
