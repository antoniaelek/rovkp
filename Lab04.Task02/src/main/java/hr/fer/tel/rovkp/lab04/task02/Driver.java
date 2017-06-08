package hr.fer.tel.rovkp.lab04.task02;

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
    private static final String IN_FILE = "./src/main/resources/DeathRecords.csv";
    private static final String OUT_PATH_1 = "./target/1.txt";
    private static final String OUT_PATH_2 = "./target/2";
    private static final String OUT_PATH_3 = "./target/3.txt";
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
        JavaRDD<USDeathRecord> records = sc.textFile(IN_FILE)
                .filter(l -> USDeathRecord.isParsable(l))
                .map(l -> new USDeathRecord(l))
                .cache();      

        // Koliko je ženskih osoba umrlo u lipnju kroz čitav period?
        long out1 = records
                .filter(n -> n.getGender().equalsIgnoreCase("F"))
                .filter(n -> n.getMonthOfDeath() == 6)
                .count();
        try {
            writeToFile(OUT_PATH_1, Long.toString(out1)); 
        } catch (IOException ex) {
            
        }
        
       // Koji dan u tjednu je umrlo najviše muških osoba starijih od 50 godina?
       JavaPairRDD<Integer, Integer> out2 = records
                .filter(n -> n.getGender().equalsIgnoreCase("M"))
                .filter(n -> n.getAge() > 60)
                .mapToPair(n -> new Tuple2<>(n.getDayOfWeekOfDeath(),1))
                .reduceByKey((x,y) -> x + y)
                .mapToPair(np -> np.swap())
                .sortByKey(false);
        out2.saveAsTextFile(OUT_PATH_2);
        
        // Koliko osoba je bilo podvrgnuto obdukciji nakon smrti?
        long out3 = records
                .filter(n -> n.getAutopsy().equalsIgnoreCase("Y"))
                .count();
        
        try {
            writeToFile(OUT_PATH_3, Long.toString(out3)); 
        } catch (IOException ex) {
            
        }
        
        // Kakvo je kretanje broja umrlih muškaraca u dobi između 45 i 65 godina po mjesecima ?
        // Rezultat je (sortirana) lista tipa Pair2 (ključ je redni broj mjeseca, a vrijednost je broj umrlih muškaraca)
        JavaPairRDD<Integer, Integer> out4 = records
                .filter(n -> n.getGender().equalsIgnoreCase("M"))
                .filter(n -> n.getAge() > 45)
                .filter(n -> n.getAge() < 65)
                .mapToPair(n -> new Tuple2<>(n.getMonthOfDeath(),1))
                .reduceByKey((x,y) -> x + y)
                .sortByKey(true);
        out4.saveAsTextFile(OUT_PATH_4);
        
        // Kakvo je kretanje postotka umrlih oženjenih muškaraca u dobi između 45 i 65 godina po mjesecima?
        // Rezultat je (sortiran) skup tipa Pair2 (ključ je redni broj mjeseca, a vrijednost je postotak).
        JavaPairRDD<Integer, Integer> married = records
                .filter(n -> n.getGender().equalsIgnoreCase("M"))
                .filter(n -> n.getMaritialStatus().equalsIgnoreCase("M"))
                .filter(n -> n.getAge() > 45)
                .filter(n -> n.getAge() <= 65)
                .mapToPair(n -> new Tuple2<>(n.getMonthOfDeath(),1))
                .reduceByKey((x,y) -> x + y)
                .sortByKey(true);
        
        JavaPairRDD<Integer, String> out5 = married.join(out4)
                .mapToPair(n -> new Tuple2<>(n._1,((double)n._2._1/n._2._2*100)+"%"))
                .sortByKey(true);
        out5.saveAsTextFile(OUT_PATH_5);
        
        // Koji je ukupni broj umrlih u nesreći (kod 1) u cjelokupnom periodu?
        long out6 = records
                .filter(n -> n.getMannerOfDeath() == 1)
                .count();
        try { 
            writeToFile(OUT_PATH_6, Long.toString(out6)); 
        } catch (IOException ex) {
            
        }
        
        // Koliki je broj različitih godina starosti umrlih osoba koji se pojavljuju u zapisima?
        long numDistinctAges = records
                .mapToPair(n -> new Tuple2<>(n.getAge(),1))
                .distinct()
                .count();
        
        try { 
            writeToFile(OUT_PATH_7, Long.toString(numDistinctAges)); 
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

