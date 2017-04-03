/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework02.task01;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.WritableComparable;

/**
 *
 * @author aelek
 */
public class TripTimesTuple implements WritableComparable<TripTimesTuple> {

    private IntWritable total;
    private IntWritable min;
    private IntWritable max;
    
    public TripTimesTuple() {
        set();
    }

    public TripTimesTuple(int total, int min, int max) {
        set(total, min, max);
    }

    public TripTimesTuple(IntWritable total, IntWritable min, IntWritable max) {
        set(total, min, max);
    }

    public int getTotal() {
        return total.get();
    }

    public int getMin() {
        return min.get();
    }
    
    public int getMax() {
        return max.get();
    }
    
    @Override
    public void write(DataOutput out) throws IOException {
        total.write(out);
        min.write(out);
        max.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        total.readFields(in);
        min.readFields(in);
        max.readFields(in);
    }

    @Override
    public int hashCode() {
        return total.hashCode() * 163 + min.hashCode() * 163 + max.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TripTimesTuple) {
            TripTimesTuple tp = (TripTimesTuple) o;
            return total.equals(tp.total) && min.equals(tp.min) && max.equals(tp.max);
        }
        return false;
    }

    @Override
    public String toString() {
        return total.toString() + "\t" + min.toString() + "\t" + max.toString();
    }

    @Override
    public int compareTo(TripTimesTuple tp) {
        int cmp = total.compareTo(tp.total);
        if (cmp != 0) {
            return cmp;
        }
        
        cmp = max.compareTo(tp.max);
        if (cmp != 0) {
            return cmp;
        }
        
        return min.compareTo(tp.min);
    }
    
    private void set(){
        this.total = new IntWritable();
        this.min = new IntWritable();
        this.max = new IntWritable();
    }
    
    private void set(IntWritable total, IntWritable min, IntWritable max) {
        this.total = total;
        this.min = min;
        this.max = max;
    }

    void set(int total, int min, int max) {
        this.total = new IntWritable(total);
        this.min = new IntWritable(min);
        this.max = new IntWritable(max);
    }
}
