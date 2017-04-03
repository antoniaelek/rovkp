/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework02.task01;

import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 *
 * @author aelek
 */
public class DrivesReducer extends Reducer<Text, DistancesTuple, Text, DistancesTuple> {
    
    private DistancesTuple result = new DistancesTuple();
        
    @Override
    protected void reduce(Text key, Iterable<DistancesTuple> values, Context context) throws IOException, InterruptedException {
        Iterator<DistancesTuple> iterator = values.iterator();
        if (!iterator.hasNext()) return;
        
        DistancesTuple first = iterator.next();
        int total = first.getTotal();
        int min = first.getMin();
        int max = first.getMax();
        
        while (iterator.hasNext())
        {
            DistancesTuple curr = iterator.next();
            total += curr.getTotal();
            min = curr.getMin() < min ? curr.getMin() : min;
            max = curr.getMax() > max ? curr.getMax() : max;
        }
        result.set(total,min,max);
        context.write(key, result);
    }
    
}
