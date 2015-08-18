package ro.unibuc.nlp.cognates.metrics;

import org.junit.Assert;
import org.junit.Test;

public class JaccardTest extends MetricTest{

	public JaccardTest() {
		
		metric = new Jaccard();
	}
	
	@Test
	@Override
	public void testMetric() {
		
		// distance
		Assert.assertEquals(0.75, metric.computeDistance("langue", "lingua"), DELTA);
		Assert.assertEquals(0.75, metric.computeDistance("spera", "espérer"), DELTA);
		Assert.assertEquals(0.67, metric.computeDistance("an", "anno"), DELTA);
		
		// similarity
		Assert.assertEquals(0.25, metric.computeSimilarity("langue", "lingua"), DELTA);
		Assert.assertEquals(0.25, metric.computeSimilarity("spera", "espérer"), DELTA);
		Assert.assertEquals(0.33, metric.computeSimilarity("an", "anno"), DELTA);
	}

	@Test
	@Override
	public void testEqualValues() {
		
		equalValues();
	}

	@Test
	@Override
	public void testCornerCases() {
		
		cornerCases();
	}

	@Test
	@Override
	public void testValidation() {
		
		nullValues();
	}
}