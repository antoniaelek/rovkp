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
import java.io.IOException;
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

        Job firstJob = Job.getInstance();
        firstJob.setJarByClass(Program.class);
        firstJob.setJobName("Locations");

        FileInputFormat.addInputPath(firstJob, new Path(args[0]));
        FileOutputFormat.setOutputPath(firstJob, new Path(INTERMEDIATE_PATH));

        firstJob.setMapperClass(LocationsMapper.class);
        firstJob.setPartitionerClass(LocationsPartitioner.class);
        firstJob.setReducerClass(LocationsReducer.class);
        firstJob.setNumReduceTasks(6);

        firstJob.setOutputKeyClass(IntWritable.class);
        firstJob.setOutputValueClass(Text.class);
        
        MultipleOutputs.addNamedOutput(firstJob, "bins", TextOutputFormat.class, NullWritable.class, Text.class);
        
        int code = firstJob.waitForCompletion(true) ? 0 : 1;
        
        System.out.println("First job return code: " + code);
        
        if (code == 0) {
            
            Job job1 = run(INTERMEDIATE_PATH + "center1", 
                           args[1] + "/1");
            Job job2 = run(INTERMEDIATE_PATH + "not_center1", 
                           args[1] + "/2");
            Job job3 = run(INTERMEDIATE_PATH + "center2", 
                           args[1] + "/3");
            Job job4 = run(INTERMEDIATE_PATH + "not_center2", 
                           args[1] + "/4");
            Job job5 = run(INTERMEDIATE_PATH + "center4", 
                           args[1] + "/5");
            Job job6 = run(INTERMEDIATE_PATH + "not_center4", 
                           args[1] + "/6");

            while (!(job1.isComplete() && job2.isComplete() && 
                     job3.isComplete() && job4.isComplete() && 
                     job5.isComplete() && job6.isComplete())){
                Thread.sleep(2000);
            }
        }
        FileSystem.get(firstJob.getConfiguration())
                .delete(new Path(INTERMEDIATE_PATH), true);
    }
    
    private static Job run(String pathIn, String pathOut) 
            throws IOException, InterruptedException, ClassNotFoundException{
        Job nextJob = Job.getInstance();
        nextJob.setJarByClass(Program.class);
        nextJob.setJobName("TripTimes");

        FileInputFormat.addInputPath(nextJob, new Path(pathIn));
        FileOutputFormat.setOutputPath(nextJob, new Path(pathOut));

        nextJob.setMapperClass(TripTimesMapper.class);
        nextJob.setReducerClass(TripTimesReducer.class);

        nextJob.setOutputKeyClass(Text.class);
        nextJob.setOutputValueClass(TripTimesTuple.class);
        
        nextJob.submit();
        
        return nextJob;
    }
}
