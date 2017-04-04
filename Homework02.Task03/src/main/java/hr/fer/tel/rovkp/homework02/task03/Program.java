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
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

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
        job.getConfiguration().set("mapred.textoutputformat.separatorText", ",");

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
        
        if (code == 0) {
            Job bJob = Job.getInstance();
            bJob.setJarByClass(Program.class);
            bJob.setJobName("TripTimes");

            FileInputFormat.addInputPath(bJob, new Path(INTERMEDIATE_PATH));
            FileOutputFormat.setOutputPath(bJob, new Path(args[1]));

            bJob.setMapperClass(TripTimesMapper.class);
            bJob.setCombinerClass(TripTimesReducer.class);
            bJob.setReducerClass(TripTimesReducer.class);
            
            bJob.setOutputKeyClass(Text.class);
            bJob.setOutputValueClass(TripTimesTuple.class);

            bJob.waitForCompletion(true);

            bJob.waitForCompletion(true);
        }
    }
}
