package ro.unibuc.nlp.cognates.metrics;

/**
 * Computes the longest common subsequence distance or similarity between two input strings
 * 
 * @author alina
 */
public class Lcsr implements Metric {

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
	
	public double computeDistance(String a, String b) throws IllegalArgumentException {
		
		double similarity = computeSimilarity(a, b);
		
		return 1 - similarity;
	}

	public double computeSimilarity(String a, String b) throws IllegalArgumentException {

		double distance = computeLcs(a, b);
		int maxLength = Math.max(a.length(), b.length());

		if (maxLength == 0) {
			return 1;
		}
		
		return distance/maxLength;
	}
}