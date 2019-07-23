package ro.unibuc.nlp.cognates.production;

import ro.unibuc.nlp.cognates.metrics.Edit;
import ro.unibuc.nlp.cognates.utils.FileUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class StatisticsUtils {
	// format word1_word2 ; I must remove - characters, because the words are aligned
	public static void printStatistics(String inFile) throws IOException {
		List<String> lines = FileUtils.readLines(inFile);
		
		double leftSum = 0;
		double rightSum = 0;
		double editDistance = 0;
		
		for (String line : lines)
		{
			line = line.replace("-", "");
			String[] split = line.split("_");
			
			leftSum += split[0].length();
			rightSum += split[1].length();
			editDistance += new Edit().computeDistance(split[0], split[1]);
		}
		
		System.out.println(inFile);
		
		BigDecimal bd = new BigDecimal(leftSum / lines.size()).setScale(2, RoundingMode.HALF_UP);
		System.out.println("left avg word length: " + bd.doubleValue());
		
		bd = new BigDecimal(rightSum / lines.size()).setScale(2, RoundingMode.HALF_UP);
		System.out.println("right avg word length: " + bd.doubleValue());
		
		bd = new BigDecimal(editDistance / lines.size()).setScale(2, RoundingMode.HALF_UP);
		System.out.println("avg edit distance: " + bd.doubleValue());
		
		System.out.println();
	}
	
	// format l1_l2_prod1 | prod2 | ...
	public static void printMap(String inFile) throws IOException
	{
		Map<Double, Integer> map = new TreeMap<Double, Integer>();
		List<String> lines = FileUtils.readLines(inFile);
		
		for (String line : lines)
		{
			String[] split = line.split("_");
			String gold = split[1];
			String produced = split[2].split(" \\| ")[0].trim();
			
			double editDistance = new Edit().computeDistance(gold, produced);
			BigDecimal bd = new BigDecimal(editDistance).setScale(1, RoundingMode.HALF_UP);
			map.put(bd.doubleValue(), map.get(bd.doubleValue()) == null ? 1 : map.get(bd.doubleValue()) + 1);
		}
	
		System.out.println(inFile);
		for (Entry<Double, Integer> entry : map.entrySet())
		{	
			BigDecimal bd = new BigDecimal(entry.getValue() * 100.0 / lines.size()).setScale(1, RoundingMode.HALF_UP);
			System.out.println(entry.getKey() + ": " + bd.doubleValue());
		}
		System.out.println();
	}
}
