package hr.fer.tel.rovkp.homework04.task03;

import java.util.Arrays;
import java.util.NoSuchElementException;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

public class Driver {
    private static final String OUT_PATH = "/home/aelek/spark/rovkp/target/out";

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("Task03");

        //set the master if not already set through the command line
        try {
            conf.get("spark.master");
        } catch (NoSuchElementException ex) {
            //spark streaming application requires at least 2 threads
            conf.setMaster("local[2]");
        }

        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(3));
        jssc.checkpoint("/home/aelek/spark/rovkp/target/checkpoint/");
        
        JavaDStream<String> lines = jssc.socketTextStream("localhost", 10002);


        PollutionReading reading = new PollutionReading();
        JavaPairDStream<String, Integer> result = lines
                .filter(l -> reading.Parse(l))
                .map(l -> new PollutionReading(l))
                .mapToPair(o -> new Tuple2<>(o.getLatitude() + "," + o.getLongitude(), o.getOzone()))
                .reduceByKeyAndWindow(
                        (t1, t2) -> { return Math.min(t1, t2); },
                        (t1, t2) -> { return t1; },
                        new Duration(45000), 
                        new Duration(15000)
                );             


        result.dstream().saveAsTextFiles(OUT_PATH, "txt");

        jssc.start();

        jssc.awaitTermination();
    }
}
