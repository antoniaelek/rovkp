package hr.fer.tel.rovkp.lab02;

import java.io.IOException;
import java.text.ParseException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FilterMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        DebsRecordParser parser = new DebsRecordParser();
        try {
            parser.parse(value.toString());
        } catch (ParseException ex) {
            System.err.println(ex);
        }
        
        if (parser.getProfit() <= 0) return;
        if (!parser.isInArea()) return;
        
        context.write(NullWritable.get(), value);
    }
    
}
