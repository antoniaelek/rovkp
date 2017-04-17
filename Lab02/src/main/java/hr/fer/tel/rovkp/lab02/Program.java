package hr.fer.tel.rovkp.lab02;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
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
        if (args.length != 3) {
            System.err.println("Usage: <jar> [filter|hourly-export|sequential] <input path> <output path>");
            return;
        }
        
        switch(args[0].toLowerCase()){
            case "filter":
                filter(args[1], args[2], false);
                break;
            case "hourly-export":
                hourlyExport(args[1], args[2], false);
                break;
            case "sequential":
                sequential(args[1], args[2]);
                break;
            default:
                System.err.println("Usage: <jar> [filter|hourly-export] <input path> <output path>");
                break;
        }
    }
    
    public static void sequential(String in, String out) throws IOException, InterruptedException, ClassNotFoundException{
        int code = filter(in, out, true);
        System.out.println("First job return code: " + code);
        if (code == 0) {
            code = hourlyExport(in, out, true);
            System.out.println("Second job return code: " + code);
        }
    }
    
    public static int filter(String in, String out, boolean concise) throws IOException, InterruptedException, ClassNotFoundException{
        Job job = Job.getInstance();
        job.setJarByClass(Program.class);
        job.setJobName("Filter");

        FileInputFormat.addInputPath(job, new Path(in));
        FileOutputFormat.setOutputPath(job, new Path(out));

        if (concise) job.setMapperClass(FilterMapperConcise.class);
        else job.setMapperClass(FilterMapper.class);
        job.setNumReduceTasks(0);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);
        
        int code = job.waitForCompletion(true) ? 0 : 1;
        if (code != 0 && concise)
            FileSystem.get(job.getConfiguration()).delete(new Path(out), true);
        return code;
    }
    
    public static int hourlyExport(String in, String out, boolean concise) throws IOException, InterruptedException, ClassNotFoundException{
        Configuration conf = new Configuration();
        conf.set("mapred.textoutputformat.separator", ",");
        Job job = Job.getInstance(conf, "HourlyExport");
        job.setJarByClass(Program.class);
        job.setJobName("HourlyExport");
        
        FileInputFormat.addInputPath(job, new Path(in));
        FileOutputFormat.setOutputPath(job, new Path(out));

        if (concise) {
            job.setMapperClass(HourlyExportMapperConcise.class);
            job.setReducerClass(HourlyExportReducerConcise.class);
        }
        else {
            job.setMapperClass(HourlyExportMapper.class);
            job.setReducerClass(HourlyExportReducer.class);
        }
        job.setPartitionerClass(HourlyExportPartitioner.class);
        job.setNumReduceTasks(24);
        
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Text.class);
        
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);
        
        MultipleOutputs.addNamedOutput(job, "bins", TextOutputFormat.class, NullWritable.class, Text.class);
        
        int code = job.waitForCompletion(true) ? 0 : 1;
        return code;
    }
}
