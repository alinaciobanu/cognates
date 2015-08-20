package ro.unibuc.nlp.cognates.metrics;

import java.util.HashSet;
import java.util.Set;

/**
 * Computes the Jaccard distance or similarity between two input strings
 * 
 * @author alina
 */
public class Jaccard implements Metric {
	
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
	
		MetricUtils.getInstance().validate(a, b);
		
		Set<String> aNgrams = MetricUtils.getInstance().getUniqueNgrams(a, n);
		Set<String> bNgrams = MetricUtils.getInstance().getUniqueNgrams(b, n);
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
	 * Computes the Jaccard distance between the input strings, using n-grams of the given size.
	 * 
	 * @param a the first string
	 * @param b the second string
	 * @param n the n-gram size
	 * @return the Jaccard distance between the input strings
	 * @throws IllegalArgumentException
	 */
	public double computeDistance(String a, String b, int n) throws IllegalArgumentException {
		
		double similarity = computeSimilarity(a, b, n);
		
		return 1 - similarity;
	}	

	public double computeDistance(String a, String b) throws IllegalArgumentException {
		
		double similarity = computeSimilarity(a, b);
		
		return 1 - similarity;
	}

	public double computeSimilarity(String a, String b) throws IllegalArgumentException {
		
		return computeSimilarity(a, b, 2);
	}
}