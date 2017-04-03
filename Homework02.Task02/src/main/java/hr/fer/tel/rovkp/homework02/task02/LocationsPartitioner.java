/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework02.task02;

import java.text.ParseException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 *
 * @author aelek
 */
public class LocationsPartitioner extends Partitioner<IntWritable, Text> {

    @Override
    public int getPartition(IntWritable key, Text value, int numberOfPartitions) {
        
        DebsRecordParser parser = new DebsRecordParser();
        
        try {
            parser.parse(value.toString());        
        } catch (ParseException ex) {
            System.err.println(ex);
        }
        
        boolean center = parser.getLocation().equals("center");
        
        // passenger_count
        // 1 -> 1 
        // 2 -> 2,3
        // * -> 4
        switch(key.get()) {
            case 1:
                if (center) return 0;
                else return 1;
            case 2: 
                if (center) return 2;
                else return 3;
            default:
                if (center) return 4;
                else return 5;
        }
    }
}
