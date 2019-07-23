package ro.unibuc.nlp.cognates.metrics;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Computes the longest common subsequence distance or similarity between two input strings
 * 
 * @author alina
 */
public class Lcsr implements Metric {

	private static final Logger logger = Logger.getLogger(Lcsr.class);
	
	/**
	 * Computes the longest common subsequence between the input strings.
	 * 
	 * @param a first input string
	 * @param b second input string
	 * @return the longest common subsequence between the input strings
	 * @throws IllegalArgumentException
	 */
	public double computeLcs(String a, String b) throws IllegalArgumentException {

		MetricUtils.validate(a, b);
		
		int[][]lcsr = new int[a.length() + 1][b.length() + 1];
		
		for (int i = 0; i < a.length(); i++) {
			lcsr[i][0] = 0;
		}
		
		for (int j = 0; j < b.length(); j++) {
			lcsr[0][j] = 0;
		}
		
		for (int i = 1; i <= a.length(); i++) {
			for (int j = 1; j <= b.length(); j++) {
				if (a.charAt(i-1) == b.charAt(j-1)) {
					lcsr[i][j] = lcsr[i-1][j-1] + 1;
				} 
				else {
					lcsr[i][j] = Math.max(lcsr[i][j-1], lcsr[i-1][j]);
				}
			}
		}
				
		return (double)lcsr[a.length()][b.length()];
	}
	
	/**
	 * Computes the longest common subsequence ratio distance between the input strings.
	 * 
	 * @param a first input string
	 * @param b second input string
	 * @return the longest common subsequence ratio distance between the input strings
	 * @throws IllegalArgumentException
	 */
	public double computeDistance(String a, String b) throws IllegalArgumentException {

		logger.info("Computing the LCSR distance between strings " + a + " " + b);
		
		double similarity = computeLcs(a, b);
		int maxLength = Math.max(a.length(), b.length());

		if (maxLength == 0) {
			return 0;
		}
		
		return 1 - similarity/maxLength;
	}

	
	/**
	 * Computes the longest common subsequence ratio similarity between the input strings.
	 * 
	 * @param a first input string
	 * @param b second input string
	 * @return the longest common subsequence ratio similarity between the input strings
	 * @throws IllegalArgumentException
	 */
	public double computeSimilarity(String a, String b) throws IllegalArgumentException {

		logger.info("Computing the LCSR similarity between strings " + a + " " + b);
		
		double similarity = computeLcs(a, b);
		int maxLength = Math.max(a.length(), b.length());

		if (maxLength == 0) {
			return 1;
		}
		
		return similarity/maxLength;
	}
	
	/**
	 * Computes the longest common subsequence between the input sequences.
	 * 
	 * @param a first input sequence
	 * @param b second input sequence
	 * @return the longest common subsequence between the input sequences
	 * @throws IllegalArgumentException
	 */
	public double computeLcs(List<String> a, List<String> b) throws IllegalArgumentException {

		MetricUtils.validate(a, b);
		
		int[][]lcsr = new int[a.size() + 1][b.size() + 1];
		
		for (int i = 0; i < a.size(); i++) {
			lcsr[i][0] = 0;
		}
		
		for (int j = 0; j < b.size(); j++) {
			lcsr[0][j] = 0;
		}
		
		for (int i = 1; i <= a.size(); i++) {
			for (int j = 1; j <= b.size(); j++) {
				if (a.get(i-1).equals(b.get(j-1))) {
					lcsr[i][j] = lcsr[i-1][j-1] + 1;
				} 
				else {
					lcsr[i][j] = Math.max(lcsr[i][j-1], lcsr[i-1][j]);
				}
			}
		}
				
		return (double)lcsr[a.size()][b.size()];
	}
	
	/**
	 * Computes the longest common subsequence ratio distance between the input sequences.
	 * 
	 * @param a first input sequence
	 * @param b second input sequence
	 * @return the longest common subsequence ratio distance between the input sequences
	 * @throws IllegalArgumentException
	 */
	public double computeDistance(List<String> a, List<String> b) throws IllegalArgumentException {

		logger.info("Computing the LCSR distance between strings " + a + " " + b);
		
		double similarity = computeLcs(a, b);
		int maxLength = Math.max(a.size(), b.size());

		if (maxLength == 0) {
			return 0;
		}
		
		return 1 - similarity/maxLength;
	}

	
	/**
	 * Computes the longest common subsequence ratio similarity between the input sequences.
	 * 
	 * @param a first input sequence
	 * @param b second input sequence
	 * @return the longest common subsequence ratio similarity between the input sequences
	 * @throws IllegalArgumentException
	 */
	public double computeSimilarity(List<String> a, List<String> b) throws IllegalArgumentException {

		logger.info("Computing the LCSR similarity between sequences " + a + " " + b);
		
		double similarity = computeLcs(a, b);
		int maxLength = Math.max(a.size(), b.size());

		if (maxLength == 0) {
			return 1;
		}
		
		return similarity/maxLength;
	}
}