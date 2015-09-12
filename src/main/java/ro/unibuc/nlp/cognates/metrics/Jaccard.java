package ro.unibuc.nlp.cognates.metrics;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Computes the Jaccard distance or similarity between two input strings
 * 
 * @author alina
 */
public class Jaccard implements Metric {

	private static final Logger logger = Logger.getLogger(Jaccard.class);

	/**
	 * Computes the Jaccard similarity between the input strings, using n-grams of the given size.
	 * 
	 * @param a the first string
	 * @param b the second string
	 * @param n the n-gram size
	 * @return the Jaccard similarity between the input strings
	 * @throws IllegalArgumentException
	 */
	private double computeJaccard(String a, String b, int n) throws IllegalArgumentException {
	
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
		
		return (double) nrOfCommonNgrams / allNgrams.size();
	}
	
	/**
	 * Computes the Jaccard similarity between the input strings, using n-grams of the given size.
	 * 
	 * @param a the first string
	 * @param b the second string
	 * @param n the n-gram size
	 * @return the Jaccard similarity between the input strings
	 * @throws IllegalArgumentException
	 */
	public double computeSimilarity(String a, String b, int n) throws IllegalArgumentException {

		logger.info("Computing the Jaccard similarity between strings " + a + " " + b + " using " + n + "-grams");
		
		return computeJaccard(a, b, n);
	}

	public double computeSimilarity(String a, String b) throws IllegalArgumentException {

		logger.info("Computing the Jaccard similarity between strings " + a + " " + b + " using 2-grams");
		
		return computeJaccard(a, b, 2);
	}
	
	/**
	 * Computes the Jaccard distance between the input strings, using n-grams of the given size.
	 * 
	 * @param a the first string
	 * @param b the second string
	 * @param n the n-gram size
	 * @return the Jaccard distance between the input strings
	 * @throws IllegalArgumentException
	 */
	public double computeDistance(String a, String b, int n) throws IllegalArgumentException {

		logger.info("Computing the Jaccard distance between strings " + a + " " + b + " using " + n + "-grams");
		
		double similarity = computeJaccard(a, b, n);
		return 1 - similarity;
	}	

	/**
	 * Computes the Jaccard distance between the input strings using bi-grams.
	 * 
	 * @param a the first string
	 * @param b the second string
	 * @return the Jaccard distance between the input strings
	 * @throws IllegalArgumentException
	 */
	public double computeDistance(String a, String b) throws IllegalArgumentException {

		logger.info("Computing the Jaccard distance between strings " + a + " " + b + " using 2-grams");
		
		double similarity = computeJaccard(a, b, 2);
		return 1 - similarity;
	}
}