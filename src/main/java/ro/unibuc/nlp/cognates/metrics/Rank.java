package ro.unibuc.nlp.cognates.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class Rank implements Metric {

	private static final Logger logger = Logger.getLogger(Edit.class);

	@Override
	public double computeDistance(String a, String b)
			throws IllegalArgumentException {
		
		logger.info("Computing the rank distance between strings " + a + " " + b);
		
		double rankDistance = computeRankDistance(a, b);
		double maxDistance = a.length() * (a.length() + 1) / 2 + b.length() * (b.length() + 1) / 2;
		
		if (maxDistance == 0) {
			return 0;
		}
		
		return rankDistance/maxDistance;
	}

	@Override
	public double computeDistance(List<String> a, List<String> b)
			throws IllegalArgumentException {
		
		logger.info("Computing the rank distance between sequences " + a + " " + b);
		
		double rankDistance = computeRankDistance(a, b);
		double maxDistance = a.size() * (a.size() + 1) / 2 + b.size() * (b.size() + 1) / 2;
		
		if (maxDistance == 0) {
			return 0;
		}
		
		return rankDistance/maxDistance;
	}

	@Override
	public double computeSimilarity(String a, String b)
			throws IllegalArgumentException {
		
		logger.info("Computing the rank similarity between strings " + a + " " + b);
		
		double distance = computeRankDistance(a, b);
		double maxDistance = a.length() * (a.length() + 1) / 2 + b.length() * (b.length() + 1) / 2;
		
		if (maxDistance == 0) {
			return 1;
		}
		
		return 1 - distance/maxDistance;
	}
	
	private static double computeRankDistance(String s1, String s2)
	{
		MetricUtils.validate(s1, s2);
		
		double distance = 0d;
		
		char[] splittedS1 = s1.toCharArray();
		char[] splittedS2 = s2.toCharArray();
		
		String[] indexedS1 = indexString(splittedS1);
		String[] indexedS2 = indexString(splittedS2);
		
		Map<String, Integer> rankedS1 = buildRanking(indexedS1);
		Map<String, Integer> rankedS2 = buildRanking(indexedS2);
		
		List<String> common = new ArrayList<String>();//k
		List<String> first = new ArrayList<String>();//i
		List<String> second = new ArrayList<String>();//j
		
		for (String key : rankedS1.keySet()) {
			if (rankedS2.containsKey(key))
				common.add(key);
			else
				first.add(key);
		}
		
		for (String key : rankedS2.keySet())
			if (!common.contains(key))
				second.add(key);
		
		for (String key : common)
			distance += Math.abs(rankedS1.get(key) - rankedS2.get(key));
		
		for (String key : first)
			distance += rankedS1.get(key);

		for (String key : second)
			distance += rankedS2.get(key);

		return distance;
	}

	private static String[] indexString(char[] splittedString)
	{
		String[] indexedString = new String[splittedString.length];
		Map<Character, Integer> charMap = new HashMap<Character, Integer>();
		
		for (int i = 0; i < splittedString.length; i++)
		{
			char c = splittedString[i];
			
			if (charMap.containsKey(c))
			{
				int oldIndex = charMap.get(c);
				charMap.put(c, oldIndex + 1);
			}
			else
			{
				charMap.put(c, 0);
			}
			
			indexedString[i] = c + "" + charMap.get(c);
		}
		
		return indexedString;
	}
	
	private static Map<String, Integer> buildRanking(String[] indexedString) 
	{
		Map<String, Integer> ranking = new HashMap<String, Integer>();
		
		int length = indexedString.length;
		
		for (int i = 0; i < length; i++)
		{
			ranking.put(indexedString[i], length - i);
		}
		
		return ranking;
	}
	
	private static double computeRankDistance(List<String> s1, List<String> s2)
	{
		MetricUtils.validate(s1, s2);
		
		double distance = 0d;
		
		String[] indexedS1 = indexString(s1);
		String[] indexedS2 = indexString(s2);
		
		Map<String, Integer> rankedS1 = buildRanking(indexedS1);
		Map<String, Integer> rankedS2 = buildRanking(indexedS2);
		
		List<String> common = new ArrayList<String>();//k
		List<String> first = new ArrayList<String>();//i
		List<String> second = new ArrayList<String>();//j
		
		for (String key : rankedS1.keySet()) {
			if (rankedS2.containsKey(key))
				common.add(key);
			else
				first.add(key);
		}
		
		for (String key : rankedS2.keySet())
			if (!common.contains(key))
				second.add(key);
		
		for (String key : common)
			distance += Math.abs(rankedS1.get(key) - rankedS2.get(key));
		
		for (String key : first)
			distance += rankedS1.get(key);

		for (String key : second)
			distance += rankedS2.get(key);

		return distance;
	}

	private static String[] indexString(List<String> sequence) {
		String[] indexedString = new String[sequence.size()];
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		for (int i = 0; i < sequence.size(); i++) {
			String s = sequence.get(i);
			
			if (map.containsKey(s)) {
				int oldIndex = map.get(s);
				map.put(s, oldIndex + 1);
			}
			else {
				map.put(s, 0);
			}
			
			indexedString[i] = s + map.get(s);
		}
		
		return indexedString;
	}

}
