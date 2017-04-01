/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.lab01;

import com.google.common.base.Stopwatch;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author aelek
 */
public class Program {
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: Program [zad2 <local-from> <hdfs-to>]|[zad3 <out-file>]");
            return;
        }
        
        if (args[1].equalsIgnoreCase("zad2")){
            if (args.length < 3) {
                System.err.println("Usage: Program zad2 <local-from> <hdfs-to>");
                System.exit(1);
            }
            zad2(args[1], args[2]);
        }
        else if (args[1].equalsIgnoreCase("zad3")) {
            try {   
                zad3(args[1]);
            } catch (IOException | URISyntaxException ex) {
                System.err.println(ex);
            }
        } else {
            System.err.println("Usage: Program [zad2 <local-from> <hdfs-to>]|[zad3 <out-file>]");
            System.exit(1);
        }
    }
    
    public static void zad3(String path) throws IOException, URISyntaxException{
        Serializator.serialize(path);
        Serializator.avg(path);
    }
    
    public static void zad2(String from, String to){
        Stopwatch timer = new Stopwatch().start();

        FileReaderWriter frw = new FileReaderWriter();
        try {
            frw.work(from, to, StandardCharsets.ISO_8859_1);
        } catch (URISyntaxException | IOException ex) {
            System.err.println(ex);
        } catch (Exception ex) {
            System.err.println(ex);
        }
        
        timer.stop();
        System.out.println("Read " + frw.readLinesCount() + " lines from " + frw.readFilesCount() + " files.");
        System.out.println("Program finished in " + timer.elapsedTime(TimeUnit.SECONDS) + " seconds.");
    }
}
