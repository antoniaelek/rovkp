/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework03.task01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author aelek
 */
public class TextCollection {
   
    public Map<Integer, String> parseInputFile(String file) throws IOException {
        Path filePath = Paths.get(file);        
        Map<Integer, String> results = new HashMap<>();
        
        List<String> currentJoke = new LinkedList<>();
        LinkedList<Integer> ids = new LinkedList<>();
        
        try (Stream<String> stream = Files.lines(filePath)) {
            stream.forEach(line -> {
                if (line == null) return;
                line = line.trim();
                
                if (line.isEmpty() && !currentJoke.isEmpty()) {
                    int currentId = ids.getLast();
                    String jokeText = StringUtils.join(currentJoke, "\n");
                    jokeText = StringEscapeUtils.unescapeXml(jokeText .toLowerCase().replaceAll("\\<.*?\\>", ""));
                    if (results.putIfAbsent(currentId, jokeText) != null)
                        System.err.println("Joke with id " + currentId + "already exists. Not overwriting.");                    
                } else if (line.matches("^[0-9]+:$")){
                    ids.addLast(Integer.parseInt(line.substring(0,line.length()-2)));
                    currentJoke.clear();
                } else {
                    currentJoke.add(line);
                }
            });
        }
        
        return results;
    }
    
}
