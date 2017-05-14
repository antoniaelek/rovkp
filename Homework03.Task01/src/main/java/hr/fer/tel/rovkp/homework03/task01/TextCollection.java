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
import java.util.Map.Entry;
import java.util.stream.Stream;
import jdk.nashorn.internal.parser.Token;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 *
 * @author aelek
 */
public class TextCollection {
   
    public static Map<Integer, String> parseInputFile(String file) throws IOException {
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
                    ids.addLast(Integer.parseInt(line.substring(0,line.length()-1)));
                    currentJoke.clear();
                } else {
                    currentJoke.add(line);
                }
            });
        }
        
        return results;
    }
  
    public static void createLuceneDocs(Map<Integer, String> entries) throws IOException{
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory index = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(index, config);
        for(Entry<Integer,String> entry : entries.entrySet()) {
            addDocument(indexWriter, entry);
        }
        indexWriter.close();
    }
    
    private static void addDocument(IndexWriter w, Entry<Integer, String> entry) throws IOException {
        Document doc = new Document();
        doc.add(getIdField(entry));
        doc.add(getTextField(entry));
        w.addDocument(doc);
    }
    
    private static Field getIdField(Entry<Integer, String> entry) {
        // pohranjeno u indeks, ali ne treba biti indeksirano niti tokenizirano
        FieldType idFieldType = new FieldType();
        idFieldType.setStored(true);
        idFieldType.setTokenized(false);
        idFieldType.setIndexOptions(IndexOptions.NONE);
        return new Field("ID", entry.getKey().toString(), idFieldType);
    }
    
    private static Field getTextField(Entry<Integer, String> entry) {
        // ne treba biti pohranjeno u indeks, ali treba biti indeksirano i tokenizirano
        FieldType textFieldType = new FieldType();
        textFieldType.setStored(false);
        textFieldType.setTokenized(true);
        textFieldType.setIndexOptions(IndexOptions.DOCS);
        return new Field("text", entry.getValue(), textFieldType);
    }
}
