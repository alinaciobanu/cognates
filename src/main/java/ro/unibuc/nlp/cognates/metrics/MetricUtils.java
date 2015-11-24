package ro.unibuc.nlp.cognates.metrics;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Utility methods for computing distance or similarity metrics.
 * 
 * @author alina
 */
public class MetricUtils {

	private static final Logger logger = Logger.getLogger(MetricUtils.class);
	
    /**
     * Validates that the input strings are not null.
     * 
     * @param a first input string
     * @param b second input string
     * @throws IllegalArgumentException
     */
    public static void validate(String a, String b)  throws IllegalArgumentException {    	

    	logger.info("Validating input strings: [" + a + ", " + b + "]");
    	
    	if (a == null || b == null) {
    		throw new IllegalArgumentException("Input strings cannot be null.");
    	}
    }
    
    /**
     * Extracts a set of unique n-grams from the input string.
     * 
     * @param string the input string
     * @param size the size of the n-grams
     * @return a set of unique n-grams
     */
    public static Set<String> getUniqueNgrams(String string, int size) {
    	
    	return new HashSet<String>(getNgrams(string, size));
    }
    
	/**
	 * Extracts a list of n-grams from the input string.
	 * 
	 * @param string the input string
	 * @param size the size of the n-grams
	 * @return a list of n-grams
	 */
	public static List<String> getNgrams(String string, int size) {
    	
    	return getNgrams(string, size, false);
	}
	
	/**
	 * Extracts a list of n-grams from the input string.
	 * 
	 * @param string the input string
	 * @param size the maximum size of the n-grams
	 * @param range <code>true</code> if all n-gram length up to 'size' ar used,
	 * <code>false</code> otherwise
	 * @return a list of n-grams
	 */
	public static List<String> getNgrams(String string, int size, boolean range) {
    	
		List<String> ngrams = new LinkedList<String>();
		
		for (int j = 1; j <= size; j++) {
			logger.info("Extracting " + j + "-grams from string" + string);
			for (int i = 0; i <= string.length() - size; i++) {
				String ngram = getNgram(string, i, size); 
				if (!"".equals(ngram))
					ngrams.add(ngram);
			}
		}
		
		return ngrams;
	}
	
	/**
	 * Extracts an n-gram from the input string.
	 * 
	 * @param string the input string
	 * @param start the index where the n-gram begins
	 * @param size the size of the n-gram
	 * @return an n-gram (or an empty string, if the parameters are invalid)
	 */
    public static String getNgram(String string, int start, int size) {
		
		String ngram = "";
		
		if (string == null || start + size > string.length())
			return ngram;
			
		for (int i = 0; i < size; i++) {
			ngram += string.charAt(start + i);
		}
		
		return ngram;
	}
    
    /**
     * Formats the input value with the provided format.
     * 
     * @param value the double value to be formatted
     * @param before the number of digits before the floating point
     * @param after the number of digits after the floating point
     * @return the formatted value as a string
     * @throws IllegalArgumentException
     */
    public static String format(double value, int before, int after) throws IllegalArgumentException {
    	
    	if (before <= 0 || after <= 0) {
    		String message = "The number of ditigs before or after the floating point must be positive";
    		logger.error(message);
    		throw new IllegalArgumentException(message);
    	}
    	
    	String format = getHastagSequence(before) + "." + getHastagSequence(after);
    	DecimalFormat formatter = new DecimalFormat(format);
    	
    	return formatter.format(value);
    }
    
    /**
     * Returns a sequence of hashtags.
     * 
     * @param size the size of the sequence
     * @return a sequence of hashtags
     */
    private static String getHastagSequence (int size) {
    	
    	String sequence = "";
    	for (int i = 0; i < size; i++) {
    		sequence += "#";
    	}
    	
    	return sequence;
    }
    
    public static void main(String[] args) {
    	validate("123", "456");
    }
}