package ro.unibuc.nlp.cognates.metrics;

import java.util.Arrays;

import org.junit.Assert;

public abstract class MetricTest {

	public static final double DELTA = 0.01;
	
	protected Metric metric;
	
	public abstract void testCornerCases();
	
	public abstract void testEqualValues();
	
	public abstract void testMetric();
	
	public abstract void testValidation();
	
	protected void equalValues() {
		
		// distance
		Assert.assertEquals(0.00, metric.computeDistance("langue", "langue"), DELTA);
		
		// distance for lists
		Assert.assertEquals(0.00, metric.computeDistance(Arrays.asList("l", "a", "n", "g", "u", "e"), Arrays.asList("l", "a", "n", "g", "u", "e")), DELTA);
		
		// similarity
		Assert.assertEquals(1.00, metric.computeSimilarity("langue", "langue"), DELTA);
	}
	
	protected void cornerCases() {
		
		// distance
		Assert.assertEquals(1.00, metric.computeDistance("langue", ""), DELTA);
		Assert.assertEquals(1.00, metric.computeDistance("", "espérer"), DELTA);
		Assert.assertEquals(0.00, metric.computeDistance("", ""), DELTA);
		
		// distance lists
//		Assert.assertEquals(1.00, metric.computeDistance(Arrays.asList("l", "a", "n", "g", "u", "e"), Arrays.asList()), DELTA);
//		Assert.assertEquals(1.00, metric.computeDistance(Arrays.asList(), Arrays.asList("e", "s", "p", "é", "r", "e", "r")), DELTA);
//		Assert.assertEquals(0.00, metric.computeDistance(Arrays.asList(), Arrays.asList()), DELTA);
		
		// similarity
		Assert.assertEquals(0.00, metric.computeSimilarity("langue", ""), DELTA);
		Assert.assertEquals(0.00, metric.computeSimilarity("", "espérer"), DELTA);
		Assert.assertEquals(1.00, metric.computeSimilarity("", ""), DELTA);
	}	
	
	protected void nullValues() {
		
		// distance + first argument
		try {
			metric.computeDistance(null, "espérer");
			Assert.fail("Expecting an exception for illegal arguments.");
		}
		catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
		
		// distance + first argument
//		try {
//			metric.computeDistance(null, Arrays.asList("e", "s", "p", "é", "r", "e", "r"));
//			Assert.fail("Expecting an exception for illegal arguments.");
//		}
//		catch (Exception e) {
//			Assert.assertTrue(e instanceof IllegalArgumentException);
//		}
		
		// similarity + second argument
		try {
			metric.computeSimilarity("an", null);
			Assert.fail("Expecting an exception for illegal arguments.");
		}
		catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
	}
}