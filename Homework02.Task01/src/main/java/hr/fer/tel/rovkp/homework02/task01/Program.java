/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework02.task01;

import com.google.common.base.Stopwatch;
import java.util.concurrent.TimeUnit;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 * @author aelek
 */
public class Program {
    public static void main(String[] args) throws Exception {
        Stopwatch timer = new Stopwatch();
        timer.start();
        
        if (args.length != 2) {
            timer.stop();
            System.err.println("Usage: <jar> <input path> <output path>");
            return;
        }

        Job job = Job.getInstance();
        job.setJarByClass(Program.class);
        job.setJobName("TripTimes");

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(TripTimesMapper.class);
        job.setCombinerClass(TripTimesReducer.class);
        job.setReducerClass(TripTimesReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(TripTimesTuple.class);
        
        job.waitForCompletion(true);
        timer.stop();
        System.out.println("Total time: " + timer.elapsedTime(TimeUnit.SECONDS) + "s");
    }
}
