package hr.fer.tel.rovkp.lab02;

import java.io.IOException;
import java.text.ParseException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class HourlyExportMapper 
        extends Mapper<LongWritable, Text, IntWritable, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        DebsRecordParser parser = new DebsRecordParser();
        try {
            parser.parse(value.toString());
        } catch (ParseException ex) {
            System.err.println(ex);
        }

        context.write(new IntWritable(parser.getHour()),value);
    }
    
}
