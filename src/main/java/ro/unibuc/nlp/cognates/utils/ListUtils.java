package ro.unibuc.nlp.cognates.utils;

import java.util.LinkedList;
import java.util.List;

public class ListUtils {
	
	/**
	 * Computes the intersection of the given lists
	 * 
	 * @param lists a list of lists
	 * @return a list containing the elements that exist in all the input lists
	 */
	public static List<String> computeIntersection (List<List<String>> lists) {
		
		List<String> intersection = new LinkedList<String>();
		
		if (lists == null || lists.size() == 0) {
			return intersection;
		}
		
		intersection.addAll(lists.get(0));
		
		for (int i = 1; i < lists.size(); i++) {
			intersection.retainAll(lists.get(i));
		}
		
		return intersection;
	}
}
