/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework02.task02;

import java.io.IOException;
import java.text.ParseException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 *
 * @author aelek
 */
public class LocationsMapper extends Mapper<LongWritable, Text, IntWritable, Text> {

    
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String record = value.toString();
        
        // Skip headers
        if (key.get() == 0) return;
        
        DebsRecordParser parser = new DebsRecordParser();
        try {
            parser.parse(record);
            context.write(new IntWritable(parser.getPassengerCategory()), new Text(parser.getInput()));
        } catch (ParseException ex) {
            System.err.println(ex);
        }
    }

    
}
