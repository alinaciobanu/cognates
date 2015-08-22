package ro.unibuc.nlp.cognates.metrics;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class MetricUtilsTest {

	@Test
	public void testValidation() {
			
		// test validation on the first argument
		try {
			MetricUtils.validate(null, "test");
			Assert.fail("Expecting an exception for illegal arguments.");
		}
		catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
		
		// test validation on the second argument
		try {
			MetricUtils.validate("test", null);
			Assert.fail("Expecting an exception for illegal arguments.");
		}
		catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	@Test
	public void testNgrams() {
		
		// 2-grams
		List<String> ngrams = MetricUtils.getNgrams("lingua", 2);
		String[] actual = ngrams.toArray(new String[ngrams.size()]) ;
		String[] expected = new String[]{"li", "in", "ng", "gu", "ua"};
		Assert.assertArrayEquals(actual, expected);
		
		// 3-grams
		ngrams = MetricUtils.getNgrams("lingua", 3);
		actual = ngrams.toArray(new String[ngrams.size()]) ;
		expected = new String[]{"lin", "ing", "ngu", "gua"};
		Assert.assertArrayEquals(actual, expected);	
		
		// 7-grams
		ngrams = MetricUtils.getNgrams("lingua", 7);
		actual = ngrams.toArray(new String[ngrams.size()]) ;
		// there are no 7-grams in a word of length 6
		expected = new String[]{};
		Assert.assertArrayEquals(actual, expected);			
	}
}