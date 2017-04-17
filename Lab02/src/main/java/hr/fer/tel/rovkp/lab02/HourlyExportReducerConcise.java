package hr.fer.tel.rovkp.lab02;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class HourlyExportReducerConcise 
        extends Reducer<IntWritable, Text, NullWritable, Text>{

    private MultipleOutputs<NullWritable, Text> mos;
    
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
       mos = new MultipleOutputs<>(context);
    }
    
    @Override
    protected void reduce(IntWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        HashMap<String, Integer> drives = new HashMap<>();
        HashMap<String, Double> profits = new HashMap<>();
        
        for (Text record : values){
            // hour;cellid;profit
            String[] concise = record.toString().split(";");
            
            String cell = concise[1];
            
            if (drives.containsKey(cell)) 
                drives.put(cell, drives.get(cell) + 1);
            else 
                drives.put(cell, 1);
            
            double profit = Double.parseDouble(concise[2]);
            if (profits.containsKey(cell)) 
                profits.put(cell, profits.get(cell) + profit);
            else 
                profits.put(cell, profit);
        }
        
        SimpleEntry<String, Integer> maxDrive = new SimpleEntry<>("", 0);
        SimpleEntry<String, Double> maxProfit = new SimpleEntry<>("", 0d);
        
        for (String s : drives.keySet()){
            int curr = drives.get(s);
            maxDrive = curr > maxDrive.getValue() ? new SimpleEntry<>(s,curr) : maxDrive; 
        }
        
        for (String s : profits.keySet()){
            double curr = profits.get(s);
            maxProfit = curr > maxProfit.getValue() ? new SimpleEntry<>(s,curr) : maxProfit; 
        }
        
        String outvalue = key.toString() + System.lineSeparator() +
                "drives: [" + maxDrive.getKey() + "] " + maxDrive.getValue() 
                + System.lineSeparator() +
                "profit: [" + maxProfit.getKey() + "] " + maxProfit.getValue();
        mos.write("bins", NullWritable.get(), outvalue, key.toString());
    }
    
    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        mos.close();
    }
}
