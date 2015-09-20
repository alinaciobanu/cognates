package ro.unibuc.nlp.cognates.detection.aligners;

import org.apache.log4j.Logger;

import ro.unibuc.nlp.cognates.metrics.MetricUtils;
import ro.unibuc.nlp.cognates.utils.StringUtils;

/**
 * Aligns strings using the Needleman-Wunsch algorithm.
 * 
 * @author alina
 */
public class NeedlemanWunsch implements Aligner {

	private static final Logger logger = Logger.getLogger(Aligner.class);
	
	/**
	 * Computes the alignment matrix for the Needleman-Wunsch algorithm.
	 */
	private int[][] computeMatrix(String string1, String string2) {
		
		logger.info("Computing the alignment matrix for strings " + 
					string1 + " " + string2);
		
		int length1 = string1.length();
		int length2 = string2.length();
		
		int[][] matrix = new int[length1 + 1][length2 + 1];
		matrix[0][0] = 0;

		for (int i = 1; i <= length1; i++) { 
			matrix[i][0] = matrix[i - 1][0] + getWeight(string1.charAt(i - 1), '\0');
		}
		for (int j = 1; j <= length2; j++) {
			matrix[0][j] = matrix[0][j - 1] + getWeight('\0', string2.charAt(j - 1));
		}
		for (int i = 1; i <= length1; i++) {
			for (int j = 1; j <= length2; j++) {
				matrix[i][j] = 
						Math.min(matrix[i - 1][j - 1] + getWeight(string1.charAt(i - 1), string2.charAt(j - 1)),
								 Math.min(matrix[i - 1][j] + getWeight(string1.charAt(i - 1), '\0'), 
										  matrix[i][j - 1] + getWeight('\0', string2.charAt(j - 1))));
			}
		}
		
		return matrix;
	}

	/**
	 * Computes the Needleman-Wunsch alignment of the input strings.
	 * 
	 * @param string1 the first input string
	 * @param string2 the second input string
	 * @return the orthographic alignment of the strings. Example:
	 * 		   Input: 'exhaustiv', 'esaustivo' 
	 * 		   Output: 'exhaustiv-_es-austivo'
	 */
	public String align(String string1, String string2) {
		
		MetricUtils.validate(string1, string2);
		
		logger.info("Computing the Needleman-Wunsch alignment for strings " +
				    string1 + " " + string2);

		int[][] matrix = computeMatrix(string1, string2);
		
		int length1 = string1.length();
		int length2 = string2.length();
		
		String aligned1 = "";
		String aligned2 = "";

		while (length1 > 0 || length2 > 0) {
			if (length1 > 0 && 
				matrix[length1][length2] == matrix[length1 - 1][length2] + 
											getWeight(string1.charAt(length1 - 1), '\0')) {
				aligned1 = string1.charAt(--length1) + aligned1;
				aligned2 = "-" + aligned2;
			} 
			else if (length2 > 0 && 
				matrix[length1][length2] == matrix[length1][length2 - 1] + 
											getWeight('\0', string2.charAt(length2 - 1))) {
				aligned1 = "-" + aligned1;
				aligned2 = string2.charAt(--length2) + aligned2;
			}
			else if (length1 > 0 && length2 > 0 && 
				matrix[length1][length2] == matrix[length1 - 1][length2 - 1] + 
											getWeight(string1.charAt(length1 - 1), string2.charAt(length2 - 1))) { 
				aligned1 = string1.charAt(--length1) + aligned1;
				aligned2 = string2.charAt(--length2) + aligned2;
			} 
		}

		return aligned1 + "_" + aligned2;
	}

	/**
	 * Computes the weight of transitioning from character char1 to character char2.
	 * @param char1 source character
	 * @param char2 target character
	 * @return the weight of the char1 -> char2 transition
	 */
	private int getWeight(char char1, char char2) {
		
		if (char1 == char2 || StringUtils.areEqual(char1 + "", char2 + "", true)) { 
			return 0;
		}
		
		return 1;
	}
	
	/**
	 * Computes the Needleman-Wunsch alignment score for string1 and string2.
	 * 
	 * @param string1 the first input string
	 * @param string2 the second input string
	 * @return the alignment score of the two strings
	 */
	public int getScore(String string1, String string2) {
		
		MetricUtils.validate(string1, string2);
		
		logger.info("Computing the Needleman-Wunsch alignment score for strings " +
					string1 + " " + string2);
		
		int[][] matrix = computeMatrix(string1, string2);
		
		return matrix[string1.length()][string2.length()];
	}
}