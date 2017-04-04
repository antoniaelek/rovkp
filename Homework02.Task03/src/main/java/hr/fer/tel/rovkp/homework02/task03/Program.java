/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework02.task03;

import hr.fer.tel.rovkp.homework02.task01.TripTimesMapper;
import hr.fer.tel.rovkp.homework02.task01.TripTimesReducer;
import hr.fer.tel.rovkp.homework02.task01.TripTimesTuple;
import hr.fer.tel.rovkp.homework02.task02.LocationsMapper;
import hr.fer.tel.rovkp.homework02.task02.LocationsPartitioner;
import hr.fer.tel.rovkp.homework02.task02.LocationsReducer;
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

/**
 *
 * @author aelek
 */
public class Program {
    
    private static final String INTERMEDIATE_PATH = "intermediate";
    
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: <jar> <input path> <output path>");
            return;
        }

        Job job = Job.getInstance();
        job.setJarByClass(Program.class);
        job.setJobName("Locations");
        // job.getConfiguration().set("mapred.textoutputformat.separatorText", ",");

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(INTERMEDIATE_PATH));

        job.setMapperClass(LocationsMapper.class);
        job.setPartitionerClass(LocationsPartitioner.class);
        job.setReducerClass(LocationsReducer.class);
        job.setNumReduceTasks(6);

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);
        
        int code = job.waitForCompletion(true) ? 0 : 1;
        
        System.out.println("First job return code: " + code);
        
        if (code != 0) 
            FileSystem.get(job.getConfiguration()).delete(new Path(INTERMEDIATE_PATH), true);
        else {
            Job nextJob = Job.getInstance();
            nextJob.setJarByClass(Program.class);
            nextJob.setJobName("TripTimes");

            FileInputFormat.addInputPath(nextJob, new Path(INTERMEDIATE_PATH));
            FileOutputFormat.setOutputPath(nextJob, new Path(args[1]));

            nextJob.setMapperClass(TripTimesMapper.class);
            // nextJob.setCombinerClass(TripTimesReducer.class);
            nextJob.setReducerClass(TripTimesReducer.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(TripTimesTuple.class);

            //configure the multiple outputs
            MultipleOutputs.addNamedOutput(nextJob, "bins", TextOutputFormat.class, Text.class, TripTimesTuple.class);
            
            nextJob.waitForCompletion(true);

            nextJob.waitForCompletion(true);
            
            FileSystem.get(job.getConfiguration()).delete(new Path(INTERMEDIATE_PATH), true);
        }
    }
}
