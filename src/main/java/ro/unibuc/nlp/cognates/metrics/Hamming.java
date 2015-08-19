package ro.unibuc.nlp.cognates.metrics;

/**
 * Computes the Hamming distance or similarity between two input strings.
 * 
 * @author alina
 */
public class Hamming implements Metric {
	
	/**
	 * Computes the unnormalized Hamming distance between the input strings.
	 * 
	 * @param a the first string
	 * @param b the second string
	 * @return the unnormalized Hamming distance between the input strings
	 * @throws IllegalArgumentException
	 */
	public double computeHamming(String a, String b) throws IllegalArgumentException {
		
		MetricUtils.getInstance().validate(a, b);
		
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

	public double computeDistance(String a, String b) throws IllegalArgumentException {

		double distance = computeHamming(a, b);
		int maxLength = Math.max(a.length(), b.length());
		
		if (maxLength == 0) {
			return 0;
		}
					
		return distance/maxLength;
	}

	public double computeSimilarity(String a, String b) throws IllegalArgumentException {

		double distance = computeDistance(a, b);
		
		return 1 - distance;
	}
}