package ro.unibuc.nlp.cognates.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.io.Resources;

public class FileUtilsTest {

	@Test
	public void testReadLines() {
		
		String path = Resources.getResource("utils/file-ok.txt").getPath();
		
		try {
			List<String> lines = FileUtils.readLines(path);
		
			Assert.assertNotNull(lines);
			Assert.assertEquals(7, lines.size());
			for (int i = 0; i < lines.size(); i++) {
				Assert.assertEquals("line" + (i + 1), lines.get(i));
			}
		}
		catch (IOException e) {
			Assert.fail("Unexpected error");
		}
	}
	
	@Test
	public void testWriteLines() throws IOException {
		
		String fileName = "file-utils-writing-test.txt";
		
		try {
			List<String> lines = new LinkedList<String>();
			for (int i = 0; i < 5; i++) {
				lines.add("line" + (i + 1));
			}
			
			FileUtils.writeLines(fileName, lines);
			lines = FileUtils.readLines(fileName);
			
			Assert.assertNotNull(lines);
			Assert.assertEquals(5, lines.size());
			for (int i = 0; i < lines.size(); i++) {
				Assert.assertEquals("line" + (i + 1), lines.get(i));
			}
		}
		catch (IOException e) {
			Assert.fail("Unexpected error");
		}
		
		// clean up
		Path path = FileSystems.getDefault().getPath(fileName);
		Files.delete(path);
	}

	@Test
	public void getReader() {

		String path = Resources.getResource("utils/file-ok.txt").getPath();
		
		try {
	        BufferedReader in = FileUtils.getReader(path);
	        Assert.assertNotNull(in);
	
	        String line;
	        int index = 0;
       
            while ((line = in.readLine()) != null) {
                Assert.assertEquals("line" + (++index), line);
            } 
        } 
        catch (IOException e) {
            Assert.fail("Unexpected error");
        }
	}

	@Test
	public void getWriter() throws IOException {
		
    	String fileName = "file-utils-writing-test.txt";
    	
        try {
    		BufferedWriter out = FileUtils.getWriter(fileName);
    		Assert.assertNotNull(out);
    		
            for (int i = 0; i < 5; i++) {
                out.write("line" + (i + 1) + "\n");
            }

            out.close();
        } 
        catch (IOException e) {
            Assert.fail("Unexpected error");
        }
		
		// clean up
		Path path = FileSystems.getDefault().getPath(fileName);
		Files.delete(path);
	}
	
	@Test
	public void testReadEmptyFile() {
		
		String path = Resources.getResource("utils/file-empty.txt").getPath();
		
		try {
			List<String> lines = FileUtils.readLines(path);
		
			Assert.assertNotNull(lines);
			Assert.assertEquals(0, lines.size());
		}
		catch (IOException e) {
			Assert.fail("Unexpected error");
		}
	}
	
	@Test
	public void testReadWithDiacritics() {
		
		String path = Resources.getResource("utils/file-diacritics.txt").getPath();
		
		try {
			List<String> lines = FileUtils.readLines(path);
		
			Assert.assertNotNull(lines);
			Assert.assertEquals(5, lines.size());

			String[] actual = lines.toArray(new String[lines.size()]) ;
			String[] expected = new String[]{"măr", "înainte", "câine", "șarpe", "țară"};
			Assert.assertArrayEquals(expected, actual);
		}
		catch (IOException e) {
			Assert.fail("Unexpected error");
		}
	}
	
	@Test
	public void testWriteEmptyFile() throws IOException {
		
		String fileName = "file-utils-writing-test.txt";
		
		try {
			List<String> lines = new LinkedList<String>();
			
			FileUtils.writeLines(fileName, lines);
			lines = FileUtils.readLines(fileName);
			
			Assert.assertNotNull(lines);
			Assert.assertEquals(0, lines.size());
		}
		catch (IOException e) {
			Assert.fail("Unexpected error");
		}
		
		// clean up
		Path path = FileSystems.getDefault().getPath(fileName);
		Files.delete(path);
	}
	
	@Test
	public void testWriteWithDiacritics() throws IOException {
		
		String fileName = "file-utils-writing-test.txt";
		String[] words = new String[]{"măr", "înainte", "câine", "șarpe", "țară"};
		
		try {
			List<String> lines = Arrays.asList(words);
			
			FileUtils.writeLines(fileName, lines);
			lines = FileUtils.readLines(fileName);
			
			Assert.assertNotNull(lines);
			Assert.assertEquals(5, lines.size());
			
			String[] actual = lines.toArray(new String[lines.size()]);
			Assert.assertArrayEquals(words, actual);			
		}
		catch (IOException e) {
			Assert.fail("Unexpected error");
		}
		
		// clean up
		Path path = FileSystems.getDefault().getPath(fileName);
		Files.delete(path);
	}
}