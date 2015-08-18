package ro.unibuc.nlp.cognates.metrics;

import org.junit.Assert;
import org.junit.Test;

public class EditTest extends MetricTest {

	public EditTest() {
		
		metric = new Edit();
	}
	
	@Test
	@Override
	public void testMetric() {
		
		// distance
		Assert.assertEquals(0.33, metric.computeDistance("langue", "lingua"), DELTA);
		Assert.assertEquals(0.57, metric.computeDistance("spera", "espérer"), DELTA);
		Assert.assertEquals(0.50, metric.computeDistance("an", "anno"), DELTA);
		
		// similarity
		Assert.assertEquals(0.67, metric.computeSimilarity("langue", "lingua"), DELTA);
		Assert.assertEquals(0.43, metric.computeSimilarity("spera", "espérer"), DELTA);
		Assert.assertEquals(0.50, metric.computeSimilarity("an", "anno"), DELTA);
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