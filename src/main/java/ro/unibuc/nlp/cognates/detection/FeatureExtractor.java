package ro.unibuc.nlp.cognates.detection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ro.unibuc.nlp.cognates.detection.aligners.NeedlemanWunsch;
import ro.unibuc.nlp.cognates.metrics.Metric;
import ro.unibuc.nlp.cognates.utils.FileUtils;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.NonSparseToSparse;

/**
 * Extracts features from aligned strings.
 * 
 * @author alina
 */
public class FeatureExtractor {

	private static final Logger logger = Logger.getLogger(FeatureExtractor.class);
	/**
	 * Extracts the set of features from the alignment of strings string1 and string2, using n-grams of size n
	 * @param string1 first word
	 * @param string2 second word
	 * @param n size of the n-grams
	 * @return set of features extracted from aligning strings a and b
	 */
	public Set<String> getFeatures(String string1, String string2, int n, boolean useRange) {
		
		NeedlemanWunsch nw = new NeedlemanWunsch();
		String alignment = nw.align(string1, string2);
		String aligned1 = "$" + alignment.split("_")[0] + "$";
		String aligned2 = "$" + alignment.split("_")[1] + "$";


		int beginning = useRange ? 1 : n; // initial n-gram size
		Set<String> features = new HashSet<String>();
		for (int k = beginning; k <= n; k++) {
			for (int l = 0; l <= aligned1.length() - k; l++) {
				String feature1 = aligned1.substring(l, l + k);
				String feature2 = aligned2.substring(l, l + k);
				
				if (!feature1.equals(feature2) || feature1.contains("-") || feature2.contains("-")) {
					features.add(feature1 + "_" + feature2);
				}
			}
		}
		
		return features;
	}

	/**
	 * Extracts the set of n-gram alignment features for all the input files.
	 *  
	 * @param paths the input files containing aligned pairs of words
	 * 		  Input file format: word1____word2____label
	 * @param n the size of the n-grams
	 * @return the set of alignment features extracted from the input files
	 */
	public Set<String> getAllFeatures(List<String> paths, int n) {
		
		return getAllFeatures(paths, n, "____");
	}
	
	/**
	 * Extracts the set of n-gram alignment features for all the input files.
	 *  
	 * @param paths the input files containing aligned pairs of words
	 * 		  Input file format: word1<delimiter>word2<delimiter>label
	 * @param n the size of the n-grams
	 * @param delimiter the sequence of characters delimiting the items
	 * @return the set of alignment features extracted from the input files
	 */
	public Set<String> getAllFeatures(List<String> paths, int n, String delimiter) {
		
		Set<String> features = new HashSet<String>();
		
		for (String path : paths) {
			try {
				List<String> lines = FileUtils.readLines(path);
				for (String line : lines) {
					String[] split = line.split(delimiter);
					if (split.length < 3) {
						logger.error("Invalid input line: " + line);
						continue;
					}
					features.addAll(getFeatures(split[0], split[1], n, true));
				}
			}
			catch (IOException e) {
				logger.error("Error while reading the content of file " + path, e);
			}
		}
		
		return features;
	}
	
	/**
	 * Extracts the set of labels from the input files.
	 * 
	 * @param paths the input files containing aligned pairs of words
	 * 		  Input file format: word1____word2____label
	 * @return the set of labels extracted from the input files
	 */
	public Set<String> getAllLabels(List<String> paths) {
		
		return getAllLabels(paths, "____");
	}
	
	/**
	 * Extracts the set of labels from the input files.
	 * 
	 * @param paths the input files containing aligned pairs of words
	 * 		  Input file format: word1<delimiter>word2<delimiter>label
	 * @param delimiter the sequence of characters delimiting the items
	 * @return the set of labels extracted from the input files
	 */
	public Set<String> getAllLabels(List<String> paths, String delimiter) {
		
		Set<String> labels = new HashSet<String>();
		
		for (String path : paths) {
			try {
				List<String> lines = FileUtils.readLines(path);
				for (String line : lines) {
					String[] split = line.split(delimiter);
					if (split.length < 3) {
						logger.error("Invalid input line: " + line);
						continue;
					}
					labels.add(split[2]);
				}
			}
			catch (IOException e) {
				logger.error("Error while reading the content of file " + path, e);
			}
		}
		
		return labels;
	}

	/**
	 * Extracts the set of n-gram alignment features and builds an ARFF file for each input file.
	 * 		Input file format: word1____word2____label
	 * 		The output file is saved in the same location as the input file, with an appended .n=x.arff extension.
	 * 
	 * @param paths the input files containing aligned pairs of words
	 * @param n the size of the n-grams
	 */
	public void writeAlignment(List<String> paths, int n) {
		
		writeAlignmentArff(paths, n, "____");
	}
	
	/**
	 * Extracts the set of n-gram alignment features and builds an ARFF file for each input file.
	 * 		Input file format: word1<delimiter>word2<delimiter>label
	 * 		The output file is saved in the same location as the input file, with an appended .n=x.arff extension.
	 * 
	 * @param paths the input files containing aligned pairs of words
	 * @param n the size of the n-grams
	 * @param delimiter the sequence of characters delimiting the items
	 */
	public void writeAlignmentArff(List<String> paths, int n, String delimiter) {
		
		Set<String> labels = getAllLabels(paths);
		List<String> features = new ArrayList<String>(getAllFeatures(paths, n));
		
		FastVector attributes = new FastVector();
		// add dummy attribute
		attributes.addElement(new Attribute("dummy"));
		// add n-gram features
		for (String feature : features) {
			attributes.addElement(new Attribute(feature));
		}
		
		FastVector classes = new FastVector();
		classes.addElement("dummy");
		for (String label : labels) {
			classes.addElement(label);
		}

		// add class label
	     attributes.addElement(new Attribute("class", classes));
		
		for (String path : paths) {
			Instances dataset = new Instances(labels.toString().replace(" ", ""), attributes, 0);
			
			List<String> lines;
			try {
				lines = FileUtils.readLines(path);
			}
			catch (IOException e) {
				logger.error("Error while reading input file " + path, e);
				continue;
			}
			
			for (String line : lines) {
				String[] split = line.split(delimiter);
				if (split.length < 3) {
					logger.error("Invalid input line: " + line);
					continue;
				}
				
				Set<String> currentFeatures = getFeatures(split[0], split[1], n, true);
				if (currentFeatures.size() == 0) {
					if (logger.isDebugEnabled())
						logger.debug("No features for line " + line + " in file " + path);
					continue;
				}
			
				// + 1 for dummy (first) attribute
				// + 1 for class label (last) attribute
				double[] values = new double[features.size() + 2];
				for (String currentFeature : currentFeatures) {
					// avoid index 0 by adding 1 (known issue)
					values[features.indexOf(currentFeature) + 1] = 1;
				}
				values[values.length - 1] = classes.indexOf(split[2]);
				dataset.add(new Instance(1.0, values));
			}
			
			try {
				NonSparseToSparse nonSparseToSparseInstance = new NonSparseToSparse(); 
			    nonSparseToSparseInstance.setInputFormat(dataset); 
			    Instances sparseDataset = Filter.useFilter(dataset, nonSparseToSparseInstance);
			    
			    BufferedWriter out = FileUtils.getWriter(path.replace(".txt", "") + ".n=" + n + ".arff");
			    out.write(sparseDataset.toString());
			    out.close();
			}
			catch (Exception e) {
				logger.error("Error while writing ARFF file for input file " + path, e);
			}
		}
	}
	
	/**
	 * Computes the distance between the pairs of aligned words and builds an ARFF file for each input file.
	 * 		Input file format: word1____word2____label
	 * 		The output file is saved in the same location as the input file, with an appended .metric=x.arff extension.
	 * 
	 * @param paths the input files containing aligned pairs of words 
	 * @param metric the metric to compute
	 */
	public void writeMetricArff(List<String> paths, Metric metric) {
		writeMetricArff(paths, metric, "____");
	}	
	
	/**
	 * Computes the distance between the pairs of aligned words and builds an ARFF file for each input file.
	 * 		Input file format: word1<delimiter>word2<delimiter>label
	 * 		The output file is saved in the same location as the input file, with an appended .metric=x.arff extension.
	 * 
	 * @param paths the input files containing aligned pairs of words 
	 * @param metric the metric to compute
	 * @param delimiter the sequence of characters delimiting the items
	 */
	public void writeMetricArff(List<String> paths, Metric metric, String delimiter) {
		FastVector attributes = new FastVector();
		
		// add dummy attribute
		attributes.addElement(new Attribute("dummy"));
		attributes.addElement(new Attribute(metric.getClass().getSimpleName()));
		
		Set<String> labels = getAllLabels(paths);
		FastVector classes = new FastVector();
		classes.addElement("dummy");
		for (String label : labels) {
			classes.addElement(label);
		}

		// add class label
	     attributes.addElement(new Attribute("class", classes));
	     
		for (String path : paths) {
			Instances dataset = new Instances(labels.toString().replace(" ", ""), attributes, 0);
			
			List<String> lines;
			try {
				lines = FileUtils.readLines(path);
			}
			catch (IOException e) {
				logger.error("Error while reading input file " + path, e);
				continue;
			}
			
			for (String line : lines) {
				String[] split = line.split(delimiter);
				if (split.length < 3) {
					logger.error("Invalid input line: " + line);
					continue;
				}
				
				// + 1 for dummy (first) attribute
				// + 1 for class label (last) attribute 
				double[] values = new double[3];
				
				values[1] = metric.computeDistance(split[0], split[1]);
				values[2] = classes.indexOf(split[2]);
				dataset.add(new Instance(1.0, values));
			}

			try {
				NonSparseToSparse nonSparseToSparseInstance = new NonSparseToSparse(); 
			    nonSparseToSparseInstance.setInputFormat(dataset); 
			    Instances sparseDataset = Filter.useFilter(dataset, nonSparseToSparseInstance);
			    
			    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(
			    		path.replace(".txt", "") + ".metric=" + metric + ".arff")), Charset.forName("UTF8")));
			    out.write(sparseDataset.toString());
			    out.close();
			}
			catch (Exception e) {
				logger.error("Error while writing ARFF file for input file " + path, e);
			}
		}
	}
}