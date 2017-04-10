package hr.fer.tel.rovkp.lab02;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class HourlyExport implements Writable {
    
    private final Text cell;

    public Text getCell() {
        return cell;
    }

    private final DoubleWritable profit;

    public DoubleWritable getProfit() {
        return profit;
    }

    private IntWritable drives;

    public IntWritable getDrives() {
        return drives;
    }

    public HourlyExport(int[] cellId, double profit) {
        String cellIdStr = cellId[0] + "." + cellId[1];
        this.cell = new Text(cellIdStr);
        this.profit = new DoubleWritable(profit);
        this.drives = new IntWritable(1);
    }
    
    public HourlyExport(Text cell, DoubleWritable profit, IntWritable drives) {
        this.cell = cell;
        this.profit = profit;
        this.drives = drives;
    }

    public HourlyExport(String cell, double profit, int drives) {
        this.cell = new Text(cell);
        this.profit = new DoubleWritable(profit);
        this.drives = new IntWritable(drives);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        cell.write(out);
        profit.write(out);
        drives.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        cell.readFields(in);
        profit.readFields(in);
        drives.readFields(in);
    }

    @Override
    public String toString() {
        return "{" + "cell=" + cell + 
                ", profit=" + profit + 
                ", drives=" + drives + '}';
    }
    
    
}
