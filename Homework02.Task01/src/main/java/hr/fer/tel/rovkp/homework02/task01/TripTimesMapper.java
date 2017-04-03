/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework02.task01;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 *
 * @author aelek
 */
public class TripTimesMapper extends Mapper<LongWritable, Text, Text, TripTimesTuple> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        DebsRecordParser parser = new DebsRecordParser();
        
        // Skip headers
        if (key.get() == 0) return;
        
        // Parse and emit
        String record = value.toString();
        try {
            parser.parse(record);
            int secs = parser.getTripTimeInSecs();
            String medallion = parser.getMedallion();
            context.write(new Text(medallion), new TripTimesTuple(secs,secs,secs));
        } catch (Exception ex) {
            System.err.println(ex);
        }
     }    
}
