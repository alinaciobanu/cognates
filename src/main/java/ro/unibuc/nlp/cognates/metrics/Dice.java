package ro.unibuc.nlp.cognates.metrics;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Computes the Dice similarity or distance between the input strings.
 * 
 * @author alina
 */
public class Dice implements Metric {

	private static final Logger logger = Logger.getLogger(Dice.class);
	
	/**
	 * Computes the Dice similarity between the input strings, using n-grams of the given size.
	 * 
	 * @param a the first string
	 * @param b the second string
	 * @param n the n-gram size
	 * @return the Dice similarity between the input strings
	 * @throws IllegalArgumentException
	 */
	private double computeDice(String a, String b, int n) throws IllegalArgumentException {
		
		MetricUtils.validate(a, b);
		
		Set<String> aNgrams = MetricUtils.getUniqueNgrams(a, n);
		Set<String> bNgrams = MetricUtils.getUniqueNgrams(b, n);
		Set<String> allNgrams = new HashSet<String>();

		allNgrams.addAll(aNgrams);
		allNgrams.addAll(bNgrams);

		if (allNgrams.size() == 0) {
			return 1;
		}

		int nrOfCommonNgrams = 0;

		for (String ngram : allNgrams) {
			if (aNgrams.contains(ngram) && bNgrams.contains(ngram)) {
				nrOfCommonNgrams++;
			}
		}
		
		return 2 * (double) nrOfCommonNgrams / (aNgrams.size() + bNgrams.size());
	}
	
	/**
	 * Computes the Dice similarity between the input strings, using n-grams of the given size.
	 * 
	 * @param a the first string
	 * @param b the second string
	 * @param n the n-gram size
	 * @return the Dice similarity between the input strings
	 * @throws IllegalArgumentException
	 */
	public double computeSimilarity(String a, String b, int n) throws IllegalArgumentException {
		
		logger.info("Computing the Dice similiarity between strings " + a + " " + b + " using " + n + "-grams");
		
		return computeDice(a, b, n);
	}
	
	/**
	 * Computes the Dice distance between the input strings, using n-grams of the given size.
	 * 
	 * @param a the first string
	 * @param b the second string
	 * @param n the n-gram size
	 * @return the Dice distance between the input strings
	 * @throws IllegalArgumentException
	 */
	public double computeDistance(String a, String b, int n) throws IllegalArgumentException {

		logger.info("Computing the Dice distance between strings " + a + " " + b + " using " + n + "-grams");
		
		double similarity = computeDice(a, b, n);
		return 1 - similarity;
	}

	/**
	 * Computes the Dice distance between the input strings using bi-grams.
	 * 
	 * @param a the first string
	 * @param b the second string
	 * @return the Dice distance between the input strings
	 * @throws IllegalArgumentException
	 */
	public double computeDistance(String a, String b) throws IllegalArgumentException {
		
		logger.info("Computing the Dice distance between strings " + a + " " + b + " using 2-grams");
		
		double similarity = computeDice(a, b, 2);
		return 1 - similarity;
	}

	/**
	 * Computes the Dice similarity between the input strings using bi-grams.
	 * 
	 * @param a the first string
	 * @param b the second string
	 * @return the Dice similarity between the input strings
	 * @throws IllegalArgumentException
	 */
	public double computeSimilarity(String a, String b) throws IllegalArgumentException {
		
		logger.info("Computing the Dice similiarity between strings " + a + " " + b + " using 2-grams");
		
		return computeDice(a, b, 2);
	}

	@Override
	public double computeDistance(List<String> a, List<String> b)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return 0;
	}
}