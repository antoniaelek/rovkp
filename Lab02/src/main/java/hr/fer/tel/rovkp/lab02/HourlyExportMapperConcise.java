package hr.fer.tel.rovkp.lab02;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class HourlyExportMapperConcise 
        extends Mapper<LongWritable, Text, IntWritable, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] concise = value.toString().split(";");
        int hour = Integer.parseInt(concise[0]);

        context.write(new IntWritable(hour), value);
    }
}
