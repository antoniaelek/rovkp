/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework02.task02;

import java.io.IOException;
import java.text.ParseException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

/**
 *
 * @author aelek
 */
public class LocationsReducer extends Reducer<IntWritable, Text, NullWritable, Text> {
    private MultipleOutputs<NullWritable, Text> mos = null;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        mos = new MultipleOutputs<>(context);
    }
    
    @Override
    public void reduce(IntWritable key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        
        DebsRecordParser parser = new DebsRecordParser();
        
        boolean passed = false;
        
        for (Text value : values) {
            if (!passed) {
                try {
                    parser.parse(value.toString());
                    passed = true;
                } catch (ParseException ex) {
                    passed = false;
                }
            }
            mos.write("bins", NullWritable.get(), value, parser.getLocation() + key.toString() + "/part");
        }
    }
        
    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        mos.close();
    }
}
