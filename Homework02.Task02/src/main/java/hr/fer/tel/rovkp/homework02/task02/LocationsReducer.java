/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework02.task02;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 *
 * @author aelek
 */
public class LocationsReducer extends Reducer<IntWritable, Text, NullWritable, Text> {
    
    @Override
    public void reduce(IntWritable key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        
        for (Text value : values) {
            String valStr = value.toString();
            String[] splitted = valStr.split(",");
            String newValue = valStr.substring(splitted[0].length()+1);
            context.write(NullWritable.get(), value);
        }
    }
}
