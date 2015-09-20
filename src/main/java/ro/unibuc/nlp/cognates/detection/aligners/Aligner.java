package ro.unibuc.nlp.cognates.detection.aligners;

/**
 * Aligns input strings.
 * 
 * @author alina
 */
public interface Aligner {

	/**
	 * Computes the alignment score for the input strings.
	 * 
	 * @param string1 the first string
	 * @param string2 the second string
	 * @return the alignment score for the input strings
	 */
	public int getScore(String string1, String string2);
	
	/**
	 * Computes the alignment of the input stings.
	 * 
	 * @param string1 the first string
	 * @param string2 the second string
	 * @return the alignment of the input strings
	 */
	public String align(String string1, String string2s);
}
