/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework02.task02;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
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
    
     public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: <jar> <input path> <output path>");
            return;
        }

        Job job = Job.getInstance();
        job.setJarByClass(Program.class);
        job.setJobName("Locations");
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(LocationsMapper.class);
        job.setPartitionerClass(LocationsPartitioner.class);
        job.setReducerClass(LocationsReducer.class);
        job.setNumReduceTasks(6);

        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);
        
        MultipleOutputs.addNamedOutput(job, "bins", TextOutputFormat.class, NullWritable.class, Text.class);
        job.waitForCompletion(true);
    }
}
