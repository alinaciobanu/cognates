package ro.unibuc.nlp.cognates.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Handles I/O operations.
 * 
 * @author alina
 */
public class FileUtils {

	private static final Logger logger = Logger.getLogger(FileUtils.class);
	
	/**
	 * Reads the content of a file using the UTF8 encoding.
	 * 
	 * @param path the location of the file to be read
	 * @return a list containing the lines of the given file
	 */
    public static List<String> readLines(String path) throws IOException {
        
    	return readLines(path, StandardCharsets.UTF_8);
    }

    /**
     * Reads the content of a file using the given encoding.
     * 
     * @param path the location of the file to be read
     * @param encoding the character set used for reading the file
     * @return a list containing the lines of the given file
     */
    public static List<String> readLines(String path, Charset encoding) throws IOException {

        logger.info("Retrieving the content of file " + path + " using encoding " + encoding.toString());
       
        BufferedReader in = getReader(path, encoding);
    	
        List<String> lines = new LinkedList<String>();
        if (in == null)
            return lines;
        
        String line;
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }

        return lines;
    }

    /**
     * Writes the content of a file using the UTF8 encoding.
     * 
     * @param path the location where the file should be persisted
     * @param lines the content to be written
     */
    public static void writeLines(String path, List<String> lines) throws IOException {
        
    	writeLines(path, lines, StandardCharsets.UTF_8);
    }

    /**
     * Writes the content of a file using the given encoding.
     * 
     * @param path the location where the file should be persisted
     * @param lines the content to be written
     * @param encoding the character set used for writing the file
     */
    public static void writeLines(String path, List<String> lines, Charset encoding) throws IOException {
        
        logger.info("Writing the content of file " + path + " using encoding " + encoding.toString()); 
        
    	BufferedWriter out = getWriter(path, encoding);

        if (out == null)
            return;

        for (String line : lines) {
            out.write(line + "\n");
        }
            
        out.close();
    }

    /**
     * Returns a reader that uses the UTF8 encoding.
     * 
     * @param path the location of the file to be read
     * @return a reader that uses the UTF8 encoding
     */
    public static BufferedReader getReader(String path) throws FileNotFoundException {
        
    	return getReader(path, StandardCharsets.UTF_8);
    }

    /**
     * Returns a reader that uses the given encoding.
     * 
     * @param path the location of the file to be read
     * @param encoding the character set used for reading the file
     * @return a reader that uses the given encoding
     */
    public static BufferedReader getReader(String path, Charset encoding) throws FileNotFoundException {
    	
            return new BufferedReader(
            	   new InputStreamReader(
            	   new FileInputStream(
            	   new File(path)), encoding));
    }

    /**
     * Returns a writer that uses the UTF8 encoding.
     * 
     * @param path the location where the file should be persisted
     * @return a reader that uses the UTF8 encoding
     */
    public static BufferedWriter getWriter(String path) throws FileNotFoundException {
        
    	return getWriter(path, StandardCharsets.UTF_8);
    }

    /**
     * Returns a writer that uses the given encoding.
     * 
     * @param path the location where the file should be persisted
     * @param encoding the character set used for writing the file
     * @return a writer that uses the given encoding
     */
    public static BufferedWriter getWriter(String path, Charset encoding) throws FileNotFoundException {
        
            return new BufferedWriter(
            	   new OutputStreamWriter(
            	   new FileOutputStream(
            	   new File(path)), encoding));
    }
}