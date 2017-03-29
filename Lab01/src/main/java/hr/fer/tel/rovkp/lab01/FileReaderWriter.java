package hr.fer.tel.rovkp.lab01;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 *
 * @author aelek
 */
public class FileReaderWriter {
    private final int[] linesCount;
    private final int[] filesCount;
    
    public FileReaderWriter(){
        linesCount = new int[1];
        filesCount = new int[1];
        linesCount[0] = filesCount[0] = 0;
    }
    
    /**
     * Get the number of read lines.
     * @return Total lines read.
     */
    public int readLinesCount(){
        return linesCount[0];
    }
    
    public int readFilesCount(){
        return filesCount[0];
    }

    /**
     * Read all files from input folder and write contents to a single output file.
     * @param from input folder.
     * @param to output file.
     * @throws IOException
     */
    public void work(String from, String to) throws IOException {
        work(from, to, Charset.defaultCharset());
    }
        
    /**
     * Read all files from input folder and write contents to a single output file.
     * @param from input folder.
     * @param to output file.
     * @param charset charset of input files.
     * @throws IOException
     */
    public void work(String from, String to, Charset charset) throws IOException{
        Path rootDir = Paths.get(from);
        Path newFile = Paths.get(to);

        Files.walkFileTree(rootDir, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                List<String> lines = Files.readAllLines(file,charset);
                //Files.write(newFile,lines,StandardOpenOption.APPEND);
                linesCount[0] += lines.size();
                filesCount[0] += 1;
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
