package ro.unibuc.nlp.cognates.production;

import ro.unibuc.nlp.cognates.detection.aligners.Aligner;
import ro.unibuc.nlp.cognates.detection.aligners.NeedlemanWunsch;
import ro.unibuc.nlp.cognates.utils.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Reconstruction {

	static boolean crfsuiteFormat = false;
	static boolean isTestFile = true;
	static boolean writeOnlyTestLabels = true;
	
	public static void writeFeatureLinesForFile(String inFile, String outFile, int n, int w) throws IOException {
		(new File(outFile.substring(0, outFile.lastIndexOf("/")))).mkdirs();
		
		if ((isTestFile || writeOnlyTestLabels) && inFile.contains("TRAIN")) {
			return;
		}
		
		List<String> lines = FileUtils.readLines(inFile);
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(
				isTestFile ? outFile.replace("TEST", writeOnlyTestLabels ? "TEST-LABEL" : "TEST-NO-LABEL").replace("DEV", writeOnlyTestLabels ? "DEV-LABEL" : "DEV-NO-LABEL")
						   : outFile)), Charset.forName("UTF8")));
		
		for (String line : lines) {
			String[] split = line.split("_");
			List<String> crfLines = getFeatureLinesForInstance(split[0], split[1], n, w);
			for (String crfLine : crfLines)
				out.write(crfLine + "\n");
			out.write("\n");
		}
		
		out.close();
	}
	
	
	public static List<String> getFeatureLinesForInstance(String alignedA, String alignedB, int n, int w) throws IOException {
		// $ and # are B and E reported in the paper
		// 0 is * reported in the paper
		
		alignedA = "$" + alignedA + "#";
		alignedB = "$" + alignedB + "#";
		
		List<String> crfSuiteLines = new ArrayList<String>();
		int i = 0, j = 0;
		
		int prefixLength = prefixLength(alignedA);
		
		int start = 1;
		int stop = Math.min(start + w, alignedA.replace("-", "").length());
		
		int index = 1;
		List<String> features = new ArrayList<String>();
		
		String label = "$" + alignedB.substring(0, prefixLength);
		
		 if (label.equals("$"))
			label = "0";
		
		features.addAll(extractFeatures(alignedA.replace("-", ""), start, stop, w, index));
		
		if (crfsuiteFormat)
			features.add("c=$");
		else
			features.add(0, "c=$");
		
		crfSuiteLines.add(getCompleteLine(features, label));
		
		for (i = 0; i <= alignedA.length() - n; i++)
		{	
			if (alignedA.charAt(i) == '-')
				continue;
			
			j = i;
			
			String source = "";
			String target = "";
			
			int startIndex = j;
			
			while (source.length() < n && j < alignedA.length())
			{
				if (alignedA.charAt(j) != '-')
				{
					source += alignedA.charAt(j);
				}
				target += alignedB.charAt(j);
				
				j++; 
			}
			
			if (source.length() < n)
				continue;
			
			if (source.contains("$") || source.contains("#"))
				continue;
			
			// add characters equivalent to gap in source word, except for the case when it's the suffix
			if (!alignedA.substring(j).matches("-+#"))
			while (j < alignedA.length() && alignedA.charAt(j) == '-')
			{
				target += alignedB.charAt(j);
				j++;
			}

			int endIndex = j;
			
			label = source.equals(target) ? "0" : target;
			
			features = new ArrayList<String>();
			
			String left = alignedA.substring(0, startIndex).replace("-", "");
			String right = alignedA.substring(endIndex, alignedA.length()).replace("-", "");
			
			start = Math.max(left.length() - w, 0);
			stop = left.length();
			
			index = Math.max(-1 * w, -1 * left.length());
			
			features.addAll(extractFeatures(left, start, stop, w, index));
			
			start = 0;
			stop = Math.min(w, right.length());
			
			index = 1;
			
			features.addAll(extractFeatures(right, start, stop, w, index));
			
			features.add(0, "c=" + source);
			
			crfSuiteLines.add(getCompleteLine(features, label));
		}
		
		features = new ArrayList<String>();
		
		start = Math.max(alignedA.replace("-", "").length() - w - 1, 0);
		stop = alignedA.replace("-", "").length() - 1;
		
		index = Math.max(-1 * w, -1 * alignedA.replace("-", "").length());
		features.addAll(extractFeatures(alignedA.replace("-", ""), start, stop, w, index));
		
		int suffixLength = alignedA.length() - alignedA.replaceAll("-*#$", "").length();
		
		label = alignedB.substring(alignedB.length() - suffixLength);
		
		 if (label.equals("#"))
			label = "0";
		
		if (crfsuiteFormat)
			features.add("c=#");
		else
			features.add(0, "c=#");
		
		crfSuiteLines.add(getCompleteLine(features, label));
		
		return crfSuiteLines;
	}

	private static String getCompleteLine(List<String> features, String label) 
	{
		return crfsuiteFormat 
				? label + " " + buildFeatureString(features) 
				:(isTestFile  
					? (writeOnlyTestLabels 
							? label 
							: buildFeatureString(features))
					: buildFeatureString(features) + " " + label);
	}
	
	public static String buildFeatureString(List<String> features)
	{
		StringBuilder sb = new StringBuilder();
		
		for (String feature : features)
			sb.append(feature + " ");
		
		return sb.toString().trim();
	}
	
	public static List<String> extractFeatures(String string, int start, int stop, int w, int index)
	{
		List<String> features = new ArrayList<String>();
		
		for (int k = 1; k <= w; k++) // size of the n-grams
		{
			int beginIndex = index;
			for (int l = start; l <= stop - k; l++)
			{
				String feature = string.substring(l, l + k);
				
				if (k == 1) // beginIndex = endIndex
					features.add("f[" + beginIndex + "]=" + feature + " ");
				else
					features.add("f[" + beginIndex + "|" + (beginIndex + k - 1) + "]=" + feature + " ");
				beginIndex++;
			}
		}
		
		return features;
	}
	
	public static int prefixLength(String string)
	{
		int prefix = 0;
		
		for (int i = 0; i < string.length(); i++)
		{
			if (string.charAt(i) == '-')
				prefix++;
			else
				break;
		}
		
		return prefix;
	}
	
	public static int suffixLength(String string)
	{
		return prefixLength(new StringBuilder(string).reverse().toString());
	}
	
	public static void alignWords(String inFile, String outFile) throws IOException

	{
		List<String> lines = FileUtils.readLines(inFile);
		String separator = "\\s+";
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outFile)), Charset.forName("UTF8")));

		
		for (String line : lines)

		{
			String left = line.split(separator)[0];
			String right = line.split(separator)[1];
			Aligner nw = new NeedlemanWunsch();
			out.write(nw.align(left, right) + "\n");
		}
		
		out.close();
	}
}
