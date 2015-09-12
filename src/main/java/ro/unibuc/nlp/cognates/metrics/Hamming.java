package ro.unibuc.nlp.cognates.metrics;

import org.apache.log4j.Logger;

/**
 * Computes the Hamming distance or similarity between two input strings.
 * 
 * @author alina
 */
public class Hamming implements Metric {

	private static final Logger logger = Logger.getLogger(Hamming.class);
	
	/**
	 * Computes the unnormalized Hamming distance between the input strings.
	 * 
	 * @param a the first string
	 * @param b the second string
	 * @return the unnormalized Hamming distance between the input strings
	 * @throws IllegalArgumentException
	 */
	public double computeHamming(String a, String b) throws IllegalArgumentException {
		
		MetricUtils.validate(a, b);
		
		if (a.length() != b.length()) {
			throw new IllegalArgumentException("Input strings cannot have different sizes.");
		}
		
		int diff = 0;
		for (int i = 0; i < a.length(); i++) {
			if (a.charAt(i) != b.charAt(i)) {
				diff++;
			}
		}
		
		return diff;
	}
	
	/**
	 * Computes the normalized Hamming distance between the input strings.
	 * 
	 * @param a the first string
	 * @param b the second string
	 * @return the normalized Hamming distance between the input strings
	 * @throws IllegalArgumentException
	 */
	public double computeDistance(String a, String b) throws IllegalArgumentException {

		logger.info("Computing the Hamming distance between strings " + a + " " + b);
		
		double distance = computeHamming(a, b);
		int maxLength = Math.max(a.length(), b.length());
		
		if (maxLength == 0) {
			return 0;
		}
					
		return distance/maxLength;
	}
	
	/**
	 * Computes the normalized Hamming similarity between the input strings.
	 * 
	 * @param a the first string
	 * @param b the second string
	 * @return the normalized Hamming similarity between the input strings
	 * @throws IllegalArgumentException
	 */
	public double computeSimilarity(String a, String b) throws IllegalArgumentException {

		logger.info("Computing the Hamming similarity between strings " + a + " " + b);
		
		double distance = computeHamming(a, b);
		int maxLength = Math.max(a.length(), b.length());
		
		if (maxLength == 0) {
			return 1;
		}
		
		return 1 - distance/maxLength;
	}
}