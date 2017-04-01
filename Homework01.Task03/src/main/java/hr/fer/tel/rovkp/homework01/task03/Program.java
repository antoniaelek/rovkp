/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework01.task03;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.LocalFileSystem;

/**
 *
 * @author aelek
 */
public class Program {
    
    public static String work(String hdfsURI, String hdfsPath, String localPath) 
            throws URISyntaxException, IOException{
        
        Configuration config = new Configuration();
        FileSystem hdfs = FileSystem.get(new URI(hdfsURI), config);
        LocalFileSystem localFileSystem = LocalFileSystem.getLocal(config);
        
        Path pathLocal = new Path(localPath);
        Path pathHdfs = new Path(hdfsPath);
        
        boolean isLocalFile = localFileSystem.isFile(pathLocal) 
                           || localFileSystem.isDirectory(pathLocal);
        boolean isHdfsFile = hdfs.isFile(pathHdfs) 
                          || hdfs.isDirectory(pathHdfs);

        return new StringBuilder().append(localPath)
                                  .append(isLocalFile ? " is not" : " is")
                                  .append(" a valid local path.\n")
                                  .append(hdfsPath)
                                  .append(isHdfsFile ? " is not" : " is")
                                  .append(" a valid hdfs path.").toString();        
    }
    
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: Program <hdfsURI> <hdfsPath> <localPath>");
            return;
        }

        String hdfsURI = args[0]; // hdfs://localhost:9000
        String hdfsPath = args[1]; // ~/user/rovkp/gutenberg.zip
        String localPath = args[2]; // /usr/rovkp/gutenberg.zip
        
        try {
            System.out.println(work(hdfsURI, hdfsPath, localPath));
        } catch (IOException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }
}
