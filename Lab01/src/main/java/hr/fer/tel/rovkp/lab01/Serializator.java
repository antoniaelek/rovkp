package hr.fer.tel.rovkp.lab01;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;

/**
 *
 * @author aelek
 */
public class Serializator {

    /**
     * Serialize random sensor readings log to file.
     * @param out out file for serialization
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void serialize(String out) throws IOException, URISyntaxException{
        Path outputPath = new Path(out);
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(new URI(out), conf);
        
        // Delete output file if it already exists  
        if (hdfs.exists(outputPath)) {
            System.out.println("File " + outputPath + " already exists, deleting it.");
            hdfs.delete(outputPath, true); 
        }
        
        Random rand = new Random();
        int randKey = 0;
        float randVal = 0f;
            
        try (SequenceFile.Writer writer = SequenceFile.createWriter(
                conf,
                SequenceFile.Writer.file(outputPath),
                SequenceFile.Writer.keyClass(new IntWritable(randKey).getClass()),
                SequenceFile.Writer.valueClass(new FloatWritable(randVal).getClass()))) {
            for (int i = 0; i < 100000; i++){
                randKey = rand.nextInt(100)+1;
                randVal = (float)Math.round((rand.nextFloat()*99.99f)*100)/100;
                
                writer.append(new IntWritable(randKey), new FloatWritable(randVal));
            }
        }
    }
    
    /**
     * Deserialize sensors log file from hdfs,
     * calculate and print average reading for each sensor.
     * @param in input file for deserialization.
     * @throws URISyntaxException
     * @throws IOException 
     */
    public static void avg(String in) throws URISyntaxException, IOException {
        Path inputFile = new Path(in);
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(new URI(in), conf);
        
        float[] sums = new float[100];
        int[] counts = new int[100];
        
        SequenceFile.Reader reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(inputFile));
        IntWritable key = new IntWritable();
        FloatWritable value = new FloatWritable();
        
        while (reader.next(key, value)){
            int keyInt = Integer.parseInt(key.toString());
            float valueFloar = Float.parseFloat(value.toString());
            counts[keyInt-1] += 1;
            sums[keyInt-1] += valueFloar;
        }
        
        for (int i = 0; i<100; i++){
            System.out.println("Senzor " + (i+1) + ": " + (sums[i]/counts[i]));
        }
    }
}
