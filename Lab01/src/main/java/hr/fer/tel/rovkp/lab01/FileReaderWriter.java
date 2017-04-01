package hr.fer.tel.rovkp.lab01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

/**
 *
 * @author aelek
 */
public class FileReaderWriter {
    private int linesCount;
    private int filesCount;
    
    public FileReaderWriter(){
        linesCount = filesCount = 0;
    }
    
    /**
     * Get the number of read lines.
     * @return Total lines read.
     */
    public int readLinesCount(){
        return linesCount;
    }
    
    public int readFilesCount(){
        return filesCount;
    }

    /**
     * Read all files from input folder and write contents to a single output file.
     * @param from input folder.
     * @param hdfsTo
     * @throws IOException
     * @throws java.net.URISyntaxException
     */
    public void work(String from, String hdfsTo) throws IOException, URISyntaxException, Exception {
        work(from, hdfsTo, Charset.defaultCharset());
    }
        
    /**
     * Read all files from input folder and write contents to a single output file.
     * @param from input folder.
     * @param hdfsTo
     * @param charset character set of input files.
     * @throws IOException
     * @throws java.net.URISyntaxException
     */
    public void work(String from, String hdfsTo, Charset charset) throws IOException, URISyntaxException, Exception {
        // HDFS initialization
        Configuration config = new Configuration();
        FileSystem hdfs = FileSystem.get(new URI(from), config);
        LocalFileSystem localFs = LocalFileSystem.getLocal(config);        
        
        // Reset counters
        linesCount = filesCount = 0;
        
        // Paths
        Path pathFrom = new Path(from);
        Path pathTo = new Path(hdfsTo);
        if (hdfs.exists(pathTo)) {
            System.out.println("File " + pathTo + " already exists, deleting it.");
            hdfs.delete(pathTo, true); 
        }
        OutputStream os = hdfs.create(pathTo);
        System.out.println("Reading...");
        
        // Reading from a file, directory, or unknown
        if (localFs.isFile(pathFrom)) {
            try (BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os) )) {
                processFile(localFs, pathFrom, writer);
            }
        }
        else if (localFs.isDirectory(pathFrom)) {
            try (BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os) )) {        
                RemoteIterator<LocatedFileStatus> it = localFs.listFiles(pathFrom, true);
                while(it.hasNext()) {
                    LocatedFileStatus fileStatus = it.next();
                    processFile(localFs, fileStatus.getPath(), writer);
                }
            }
        }
        else {
            if (hdfs.exists(pathTo)) 
                hdfs.delete(pathTo, true); 
            throw new Exception(from + " is not path to a valid file or directory.");
        }
        
    }
    
    private void processFile(FileSystem fileSystem, Path path, Writer writer) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(fileSystem.open(path)));
        filesCount += 1;

        String line = br.readLine();
        while (line != null){
            linesCount += 1;
            writer.write(line + System.lineSeparator());
            line = br.readLine();
        }
    }
}
