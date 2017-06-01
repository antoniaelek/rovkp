/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework04.task02;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class Driver {
    private static final String IN_FILE = "./src/main/resources/StateNames.csv";
    private static final String OUT_PATH_1 = "./target/1";
    private static final String OUT_PATH_2 = "./target/2";
    private static final String OUT_PATH_3 = "./target/3";
    private static final String OUT_PATH_4 = "./target/4";
    private static final String OUT_PATH_5 = "./target/5";
    private static final String OUT_PATH_6 = "./target/6.txt";
    private static final String OUT_PATH_7 = "./target/7.txt";

    public static void main(String[] argss){
        SparkConf conf = new SparkConf().setAppName("Task02");

        //set the master if not already set through the command line
        try {
            conf.get("spark.master");
        } catch (NoSuchElementException ex) {
            conf.setMaster("local");
        }

        JavaSparkContext sc = new JavaSparkContext(conf);
        
        //crate an RDD from text file lines
        JavaRDD<StateName> stateNames = sc.textFile(IN_FILE)
                .filter(l -> new StateName().Parse(l))
                .map(l -> new StateName(l))
                .cache();      

        // Koje je najnepopularnije žensko ime kroz čitav period i države?
        JavaPairRDD<Integer, String> out1 = stateNames
                .filter(n -> n.getGender().toLowerCase().equals("f"))
                .mapToPair(n -> new Tuple2<>(n.getName(),n.getCount()))
                .reduceByKey((x,y) -> x + y)
                .mapToPair(np -> new Tuple2<>(np._2, np._1))
                .sortByKey(false);
        out1.saveAsTextFile(OUT_PATH_1);
        
       // Kojih 10 muških imena su najpopularnija kroz čitav period i države?
       JavaPairRDD<Integer, String> out2 = stateNames
                .filter(n -> n.getGender().toLowerCase().equals("m"))
                .mapToPair(n -> new Tuple2<>(n.getName(),n.getCount()))
                .reduceByKey((x,y) -> x + y)
                .mapToPair(np -> new Tuple2<>(np._2, np._1))
                .sortByKey(false);
        out2.saveAsTextFile(OUT_PATH_2);
        
        // U kojoj državi je 1946. godine rođeno najviše djece oba spola?
        JavaPairRDD<Integer, String> out3 = stateNames
                .filter(n -> n.getYear() == 1946)
                .mapToPair(n -> new Tuple2<>(n.getState(),n.getCount()))
                .reduceByKey((x,y) -> x + y)
                .mapToPair(np -> np.swap())
                .sortByKey(false);
        out3.saveAsTextFile(OUT_PATH_3);
        
        // Kakvo je kretanje broja novorođene ženske djece kroz godine? 
        // Rezultat je (sortirana) lista tipa Pair2 (ključ je godina, a vrijednost je broj novorođenčadi
        JavaPairRDD<Integer, Integer> out4 = stateNames
                .filter(n -> n.getGender().toLowerCase().equals("f"))
                .mapToPair(n -> new Tuple2<>(n.getYear(),n.getCount()))
                .reduceByKey((x,y) -> x + y)
                .sortByKey(false);
        out4.saveAsTextFile(OUT_PATH_4);
        
        // Kakvo je kretanje postotka imena Mary kroz godine?
        // Rezultat je (sortiran) skup tipa Pair2 (ključ je godina,a vrijednost je postotak). 
        // Pri tome iskoristite polje iz prethodnog pitanja.
        JavaPairRDD<Integer, Integer> mary = stateNames
                .filter(n -> n.getName().equals("Mary"))
                .mapToPair(n -> new Tuple2<>(n.getYear(),n.getCount()))
                .reduceByKey((x,y) -> x + y)
                .sortByKey(false);
        
        JavaPairRDD<Integer, String> out5 = mary.join(out4)
                .mapToPair(n -> new Tuple2<>(n._1,((double)n._2._1/n._2._2*100)+"%"))
                .sortByKey(false);
        out5.saveAsTextFile(OUT_PATH_5);
        
        // Koji je ukupni broj rođene djece u cjelokupnom periodu u svim državama?
        try { 
            writeToFile(OUT_PATH_6, Long.toString(stateNames.count())); 
        } catch (IOException ex) {
            
        }
        
        // Koliki je broj različitih imena koja se pojavljuju u zapisima?
        long numDistinctNames = stateNames
                .mapToPair(n -> new Tuple2<>(n.getName(),1))
                .distinct()
                .count();
        
        try { 
            writeToFile(OUT_PATH_7, Long.toString(numDistinctNames)); 
        } catch (IOException ex) {

        }
    }
    
    private static void writeToFile(String file, List<String> elements) throws IOException{
        try(PrintWriter writer = new PrintWriter(new FileWriter(file))){
             elements.forEach(r -> {
                 writer.println(r);
             });
         }
    }
    
    private static void writeToFile(String file, String element) throws IOException{
        ArrayList<String> list = new ArrayList<>();
        list.add(element);
        writeToFile(file, list);
    }
}

