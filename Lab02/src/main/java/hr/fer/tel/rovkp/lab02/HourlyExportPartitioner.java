package hr.fer.tel.rovkp.lab02;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class HourlyExportPartitioner extends Partitioner<IntWritable, Text> {

    @Override
    public int getPartition(IntWritable key, Text value, int i) {
        return Integer.parseInt(key.toString());
    }
    
}
