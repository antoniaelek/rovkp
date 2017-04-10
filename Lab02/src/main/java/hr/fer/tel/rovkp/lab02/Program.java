package hr.fer.tel.rovkp.lab02;

import com.google.common.base.Stopwatch;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Program {
    public static void main(String[] args) throws Exception {
        Stopwatch timer = new Stopwatch();
        timer.start();
        
        if (args.length != 3) {
            timer.stop();
            System.err.println("Usage: <jar> [filter|hourly-export] <input path> <output path>");
            return;
        }
        
        switch(args[0].toLowerCase()){
            case "filter":
                Filter(args[1], args[2]);
                break;
            case "hourly-export":
                HourlyExport(args[1], args[2]);
                break;
            default:
                System.err.println("Usage: <jar> [filter|hourly-export] <input path> <output path>");
        }
        
        timer.stop();
        System.out.println("Total time: " + timer.elapsedTime(TimeUnit.SECONDS) + "s");
    }
    
    public static void Filter(String in, String out) throws IOException, InterruptedException, ClassNotFoundException{
        Job job = Job.getInstance();
        job.setJarByClass(Program.class);
        job.setJobName("Filter");

        FileInputFormat.addInputPath(job, new Path(in));
        FileOutputFormat.setOutputPath(job, new Path(out));

        job.setMapperClass(FilterMapper.class);
        job.setNumReduceTasks(0);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);
        
        job.waitForCompletion(true);
    }
    
    public static void HourlyExport(String in, String out) throws IOException, InterruptedException, ClassNotFoundException{
        Configuration conf = new Configuration();
        conf.set("mapred.textoutputformat.separator", ",");
        Job job = Job.getInstance(conf, "HourlyExport");
        job.setJarByClass(Program.class);
        job.setJobName("HourlyExport");
        
        FileInputFormat.addInputPath(job, new Path(in));
        FileOutputFormat.setOutputPath(job, new Path(out));

        job.setMapperClass(HourlyExportMapper.class);
        job.setPartitionerClass(HourlyExportPartitioner.class);
        job.setReducerClass(HourlyExportReducer.class);
        job.setNumReduceTasks(24);
        
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Text.class);
        
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);
        
        MultipleOutputs.addNamedOutput(job, "bins", TextOutputFormat.class, NullWritable.class, Text.class);
        
        job.waitForCompletion(true);
    }
}
