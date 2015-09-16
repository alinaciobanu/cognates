package ro.unibuc.nlp.cognates.utils;

import org.apache.log4j.Logger;

/**
 * Provides utility methods for strings.
 * 
 * @author alina
 */
public class StringUtils {

	private static Logger logger = Logger.getLogger(StringUtils.class);
	
	/**
	 * Verifies if the input strings are equal, given a certain relaxation for the suffix of the strings and the option to disregard diacritics.
	 * 
	 * @param string1 the first input string
	 * @param string2 the second input string
	 * @param relax the maximum number of characters at the end of each input string to ignore during the comparison
	 * @param removeDiacritics specifies whether diacritics should be ignored during the comparison
	 * @return <code>true</code> if the input strings are equal (given the conditions passed as parameters), <code>false</code> otherwise
	 * @throws IllegalArgumentException if at least one of the strings is null
	 */
	public static boolean areEqual(String string1, String string2, int relax, boolean removeDiacritics) throws IllegalArgumentException {
		
		if (string1 == null || string2 == null) {
			String message = "Invalid arguments: " + string1 + " " + string2;
			logger.error(message);
			throw new IllegalArgumentException(message);
		}
		
		if (relax < 0) {
			if (logger.isDebugEnabled())
				logger.debug("Negative relaxation parameter " + relax  + ". Setting default value to 0");
			relax = 0;
		}
		
		String message = "Comparing strings " + string1 + " and " + string2;
		if (relax > 0) {
			message += " with maximum " + relax + "suffix relaxation";
		}
		if (removeDiacritics) {
			message += " disregarding diacritics";
		}
		logger.info(message);
			
		for (int i = 0; i <= relax; i++) {
			for (int j = 0; j <= relax; j++) {
				
				String trimmed1 = string1.substring(0,  string1.length() - i);
				String trimmed2 = string2.substring(0,  string2.length() - j);
				
				if (removeDiacritics) {
					trimmed1 = DiacriticUtils.removeDiacritics(trimmed1);
					trimmed2 = DiacriticUtils.removeDiacritics(trimmed2);
				}
				
				if (trimmed1.equals(trimmed2)) {
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * Verifies if the input strings are equal, given the option to disregard diacritics.
	 * 
	 * @param string1 the first input string
	 * @param string2 the second input string
	 * @param removeDiacritics specifies whether diacritics should be ignored during the comparison
	 * @return <code>true</code> if the input strings are equal (given the conditions passed as parameters), <code>false</code> otherwise
	 * @throws IllegalArgumentException if at least one of the strings is null
	 */

	public static boolean areEqual(String string1, String string2, boolean removeDiacritics) throws IllegalArgumentException {
		
		return areEqual(string1, string2, 0, removeDiacritics);
	}

	/**
	 * Verifies if the input strings are equal, given a certain relaxation for the suffix of the strings.
	 * 
	 * @param string1 the first input string
	 * @param string2 the second input string
	 * @param relax the maximum number of characters at the end of each input string to ignore during the comparison
	 * @return <code>true</code> if the input strings are equal (given the conditions passed as parameters), <code>false</code> otherwise
	 * @throws IllegalArgumentException if at least one of the strings is null
	 */

	public static boolean areEqual(String string1, String string2, int relax) throws IllegalArgumentException {
		
		return areEqual(string1, string2, relax, false);
	}
}