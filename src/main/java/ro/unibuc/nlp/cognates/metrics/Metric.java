package ro.unibuc.nlp.cognates.metrics;

/**
 * Computes the distance or similarity between two input strings.
 * 
 * @author alina
 */
public interface Metric {
	
	/**
	 * 
	 * @param a first input string
	 * @param b second input string
	 * @return the normalized distance between the input strings
	 * @throws IllegalArgumentException
	 */
    public abstract double computeDistance(String a, String b) throws IllegalArgumentException;
   
    /**
     * 
     * @param a first input string
     * @param b second input string
     * @return the similarity between the input strings (1 - normalized_distance)
     * @throws IllegalArgumentException
     */
    public abstract double computeSimilarity(String a, String b) throws IllegalArgumentException;
}