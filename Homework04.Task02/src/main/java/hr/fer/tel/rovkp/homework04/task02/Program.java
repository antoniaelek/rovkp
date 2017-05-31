/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework04.task02;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 *
 * @author aelek
 */
public class Program {
    private static String inFile = "./src/main/resources/StateNames.csv";
    private static String outFile1 = "./target/1";
    
    public static void main(String[] argss){
        SparkConf conf = new SparkConf().setAppName("Task02");

        //set the master if not already set through the command line
        try {
            conf.get("spark.master");
        } catch (NoSuchElementException ex) {
            conf.setMaster("local");
        }

        JavaSparkContext sc = new JavaSparkContext(conf);
        
//        //crate an RDD from text file lines
//        JavaRDD<StateName> stateNames = sc.textFile(inFile)
//                .filter(l -> new StateName().Parse(l))
//                .map(l -> new StateName(l));
//
//        stateNames.cache();
//
//        //Koje je najnepopularnije žensko ime kroz čitav period i države?
//        JavaPairRDD<Integer, String> out1 = stateNames.filter(n -> n.getGender().toLowerCase().equals("f"))
//                .mapToPair(n -> new Tuple2<>(n.getName(),1))
//                .reduceByKey((x,y) -> x + y)
//                .mapToPair(np -> new Tuple2<>(np._2, np._1))
//                .sortByKey(false);
        
        JavaRDD<String> out1 = sc.textFile(inFile);
        out1.saveAsTextFile(outFile1);
        
    }
    
    private void writeToFile(String file, List<String> elements) throws IOException{
    try(PrintWriter writer = new PrintWriter(new FileWriter(file))){
             elements.forEach(r -> {
                 writer.println(r.toString());
             });
         }
    }
}
