/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework03.task01;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 *
 * @author aelek
 */
public class JokesCollection {
   
    public static Map<Integer, String> parseInputFile(String file) 
            throws IOException {
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
                    jokeText = StringEscapeUtils.unescapeXml(jokeText.toLowerCase().replaceAll("\\<.*?\\>", ""));
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
        
    /**
     * Prints similarity matrix.
     * @param similarityMatrix matrix to print.
     */
    public static void printSimilarityMatrix(float[][] similarityMatrix) {
        for (float[] similarityMatrix1 : similarityMatrix) {
            for (int j = 0; j < similarityMatrix1.length; j++) {
                System.out.printf("%10f ", similarityMatrix1[j]);
            }
            System.out.println("");
        }
    }
    
    /**
     * Outputs similarity matrix to CSV file
     * in format ID1, ID2, similarity
     * @param similarityMatrix matrix to print
     * @param file CSV file to create
     * @throws FileNotFoundException 
     */
    public static void similarityMatrixAsCsv(float[][] similarityMatrix, String file)
            throws FileNotFoundException {
        try( PrintWriter out = new PrintWriter(file)  ){
            for (int i = 0; i < similarityMatrix.length; i ++) {
                for (int j = 0; j <= i; j++) {
                    if (similarityMatrix[i][j] != 0) {
                        DecimalFormat format = new DecimalFormat("###.####");
                        out.println((j+1) + ", " 
                                  + (i+1) + ", "
                                  + format.format(similarityMatrix[i][j]));
                    }
                }
            }
        }
    }
    
    /**
     * Creates similarity matrix from the input map.
     * @param entries input map.
     * @return similarity matrix.
     * @throws IOException
     * @throws ParseException 
     */
    public static float[][] createSimilarityMatrix(Map<Integer, String> entries)
            throws IOException, ParseException{        
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory index = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        
        // create Lucene docs from map entries
        try (
            IndexWriter indexWriter = new IndexWriter(index, config)) {
            for(Entry<Integer,String> entry : entries.entrySet()) {
                addDocument(indexWriter, entry);
            }
        }
        
        // create similarity matrix
        float[][] similarityMatrix = createMatrix(entries, analyzer, index);
        
        // normalize matrix
        similarityMatrix = normalizeMatrix(similarityMatrix);
        
        return similarityMatrix;
    }

    private static float[][] createMatrix(Map<Integer, String> entries, 
            StandardAnalyzer analyzer, Directory index) 
            throws NumberFormatException, IOException, ParseException {
        int hitsNo = entries.size();
        float[][] similarityMatrix = new float[hitsNo][hitsNo];
        
        int i = 0; 
        for(Entry<Integer, String> entry : entries.entrySet()) {
            Query query = new QueryParser("text", analyzer).parse(QueryParser.escape(entry.getValue()));
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(query, hitsNo);
            for(ScoreDoc hit : docs.scoreDocs) {
                Document document = reader.document(hit.doc);
                int docId = Integer.parseInt(document.get("ID"));
                int docIndex = docId - 1;
                similarityMatrix[i][docIndex] = hit.score;
            }
            i++;
        }
        return similarityMatrix;
    }
    
    private static float[][] normalizeMatrix(float[][] similarityMatrix){
        for (int i = 0; i < similarityMatrix.length; i ++) {
            float max = similarityMatrix[i][i];
            for (int j = 0; j < similarityMatrix[i].length; j++) {
                similarityMatrix[i][j] /= max;
            }
        }
        
        for (int i = 0; i < similarityMatrix.length; i ++) {
            for (int j = 0; j <= i; j++) {
                float avg = (similarityMatrix[i][j] + similarityMatrix[j][i])/2;
                similarityMatrix[i][j] = avg;
                similarityMatrix[j][i] = avg;
            }
        }
        return similarityMatrix;
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
