package ro.unibuc.nlp.cognates.metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Computes the edit distance or similarity between two input strings.
 * 
 * @author alina
 */
public class Edit implements Metric {

	private Map<String, Double> distanceMap;
	
	public Edit() {
		distanceMap = new HashMap<String, Double>();
	}

	/**
	 * Computes the unnormalized edit distance between the input strings.
	 * 
	 * @param a the first string
	 * @param b the second string
	 * @return the unnormalized edit distance between the input strings
	 * @throws IllegalArgumentException
	 */
	public double computeEdit(String a, String b) throws IllegalArgumentException {
		
		MetricUtils.getInstance().validate(a, b);
		
		int cost = 0;

		 Double computedDistance = distanceMap.get(a + "__" + b);
		 
		 if (computedDistance != null && computedDistance != 0d) {
			 return computedDistance;
		 }
		 
		 if(a.length() == 0) {
			 return b.length();
		 }
		 else if(b.length() == 0) {
			 return a.length();
		 }
		 else {
			 if(a.charAt(0) != b.charAt(0)) {
				 cost = 1;
			 }
			 double distance =  Math.min(
					            	Math.min(computeEdit(a.substring(1), b) + 1,
					            			 computeEdit(a, b.substring(1)) + 1),
					            			 computeEdit(a.substring(1), b.substring(1)) + cost);
			 
			 distanceMap.put(a + "__" + b, distance);
			 
			 return distance;
		 }
	}
	
	public double computeDistance(String a, String b) throws IllegalArgumentException {
		
		double distance = computeEdit(a, b);
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