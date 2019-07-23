package ro.unibuc.nlp.cognates.metrics;

import java.util.List;

/**
 * Computes the distance or similarity between two input strings.
 * 
 * @author alina
 */
public interface Metric {
	
	/**
	 * Computes the normalized distance between the input strings.
	 * 
	 * @param a first input string
	 * @param b second input string
	 * @return the normalized distance between the input strings
	 * @throws IllegalArgumentException
	 */
    public abstract double computeDistance(String a, String b) throws IllegalArgumentException;
	
	/**
	 * Computes the normalized distance between the input strings.
	 * 
	 * @param a first input string
	 * @param b second input string
	 * @return the normalized distance between the input strings
	 * @throws IllegalArgumentException
	 */
    public default double computeDistance(String a, String b, boolean normalized) throws IllegalArgumentException
    {
    	System.out.println("Not implemented yet!");
    	return 0;
    }
	
	/**
	 * Computes the normalized distance between the input sequences.
	 * 
	 * @param a first input sequence
	 * @param b second input sequence
	 * @return the normalized distance between the input sequences
	 * @throws IllegalArgumentException
	 */
    public abstract double computeDistance(List<String> a, List<String> b) throws IllegalArgumentException;
   
    /**
     * Computes the normalized similarity between the input strings.
     * 
     * @param a first input string.
     * @param b second input string
     * @return the similarity between the input strings (1 - normalized_distance)
     * @throws IllegalArgumentException
     */
    public abstract double computeSimilarity(String a, String b) throws IllegalArgumentException;
}