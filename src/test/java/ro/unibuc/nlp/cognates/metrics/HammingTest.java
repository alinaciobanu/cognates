package ro.unibuc.nlp.cognates.metrics;

import org.junit.Assert;
import org.junit.Test;

public class HammingTest extends MetricTest {

	public HammingTest() {
		
		metric = new Hamming();
	}
	
	@Test
	@Override
	public void testMetric() {
		
		// distance
		Assert.assertEquals(0.33, metric.computeDistance("langue", "lingua"), DELTA);
		
		// similarity
		Assert.assertEquals(0.67, metric.computeSimilarity("langue", "lingua"), DELTA);
	}
	
	@Test
	public void testSizeValidation() {
		
		// distance
		try {
			metric.computeDistance("spera", "espérer");
			Assert.fail("Expecting an exception for illegal arguments.");
		}
		catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
		
		// similarity
		try {
			metric.computeSimilarity("an", "anno");
			Assert.fail("Expecting an exception for illegal arguments.");
		}
		catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
	}

	@Test
	@Override
	public void testEqualValues() {
		
		equalValues();
	}

	@Test
	@Override
	public void testCornerCases() {
		
		// distance
		Assert.assertEquals(0.00, metric.computeDistance("", ""), DELTA);
		
		// similarity
		Assert.assertEquals(1.00, metric.computeSimilarity("", ""), DELTA);
	}

	@Test
	@Override
	public void testValidation() {
		
		nullValues();
	}
}