package ro.unibuc.nlp.cognates.metrics;

import org.junit.Assert;
import org.junit.Test;

public class DiceTest extends MetricTest {

	public DiceTest() {
		
		metric = new Dice();
	}
	
	@Test
	@Override
	public void testMetric() {
		
		// distance
		Assert.assertEquals(0.60, metric.computeDistance("langue", "lingua"), DELTA);
		Assert.assertEquals(0.60, metric.computeDistance("spera", "espérer"), DELTA);
		Assert.assertEquals(0.50, metric.computeDistance("an", "anno"), DELTA);
		
		// similarity
		Assert.assertEquals(0.40, metric.computeSimilarity("langue", "lingua"), DELTA);
		Assert.assertEquals(0.40, metric.computeSimilarity("spera", "espérer"), DELTA);
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