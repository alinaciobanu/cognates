package ro.unibuc.nlp.cognates.etymology;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import ro.unibuc.nlp.cognates.etymology.model.Lemma;
import ro.unibuc.nlp.cognates.etymology.model.Lemmas;
import ro.unibuc.nlp.cognates.etymology.model.ModelHandler;
import ro.unibuc.nlp.cognates.etymology.model.Origin;
import ro.unibuc.nlp.cognates.utils.FileUtils;
import ro.unibuc.nlp.cognates.utils.StringUtils;

import com.google.common.base.Strings;

/**
 * Matches pairs of cognates across languages.
 * 
 * @author alina
 */
public class CognateMatcher {
	
	Logger logger = Logger.getLogger(CognateMatcher.class);

	/**
	 * Matches cognates across languages
	 * 
	 * @param translationPath the cognate candidates. Line format:
	 *   value1_language1    value1_language2
	 *   value2_language1    value2_language2
	 * @param lemmasPath1 the path to the dataset of lemmas in language1
	 * @param lemmasPath2 the path to the dataset of lemmas in language2
	 * @param outPath output path
	 * @param language1 the first language
	 * @param language2 the second language
	 */
	public void match (String translationPath, String lemmasPath1, String lemmasPath2, 
			   String outPath, String language1, String language2) {
		
		match(translationPath, lemmasPath1, lemmasPath2, outPath, language1, language2, 0);
	}
	/**
	 * Matches cognates across languages
	 * 
	 * @param translationPath the cognate candidates. Line format:
	 *   value1_language1    value1_language2
	 *   value2_language1    value2_language2
	 * @param lemmasPath1 the path to the dataset of lemmas in language1
	 * @param lemmasPath2 the path to the dataset of lemmas in language2
	 * @param outPath output path
	 * @param language1 the first language
	 * @param language2 the second language
	 * @param relax the maximum number of characters at the end of each etymon to ignore during the 
	 * etymology matching
	 */
	public void match (String translationPath, String lemmasPath1, String lemmasPath2, 
			   String outPath, String language1, String language2, int relax) {
		
		Lemmas lemmas1;
		Lemmas lemmas2;
		try {
			lemmas1 = EtymologyUtils.getLemmasWithoutEtymology(
					ModelHandler.readModel(lemmasPath1), 
					Arrays.asList(language2));
			lemmas2 = EtymologyUtils.getLemmasWithoutEtymology(
					ModelHandler.readModel(lemmasPath2), 
					Arrays.asList(language1));
		}
		catch (JAXBException | IllegalArgumentException e) {
			logger.error("Error while loading datasets");
			return;
		}
		

		Map<String, Lemma> map1 = buildLemmasMap(lemmas1);
		Map<String, Lemma> map2 = buildLemmasMap(lemmas2);
		
		List<String> lines;
		BufferedWriter out;
		try {
			lines = FileUtils.readLines(translationPath);
			out = FileUtils.getWriter(outPath);
		}
		catch (FileNotFoundException e) {
			logger.error("Output path " + outPath + "could not be found");
			return;
		}
		catch (IOException e) {
			logger.error("Error while reading translations from path " + translationPath);
			return;
		}
		
		logger.info("Matching pairs of cognates from " + language1 + " and  " + language2);
		 
		for (String line : lines)
		{	
			// comment or empty line
			if (line.startsWith("#") || "".equals(line.trim())) {
				continue;
			}
		
			
			if (line.length() < 2) {
				if (logger.isDebugEnabled()) 
					logger.debug("Skippind line " + line + 
						     " that does not follow the expected format");
				continue;
			}
		
			String[] candidate = line.split("\\s+");
		
			Lemma lemma1 = map1.get(candidate[0]);
			Lemma lemma2 = map2.get(candidate[1]);
			
			if (lemma1 == null || Strings.isNullOrEmpty(lemma1.getValue())) {
				if (logger.isDebugEnabled())
					logger.debug("Skipping invalid lemma in " + language1);
				continue;
			}
			if (lemma2 == null || Strings.isNullOrEmpty(lemma2.getValue())) {
				if (logger.isDebugEnabled())
					logger.debug("Skipping invalid lemma in " + language2);
				continue;
			}

			for (Origin origin1 : lemma1.getOrigin())
			{
				String etymology1 = origin1.getLanguage();
				String value1 = origin1.getValue();

				if (etymology1 == null || Strings.isNullOrEmpty(etymology1) ||
					value1 == null || Strings.isNullOrEmpty(value1)) {
					if (logger.isDebugEnabled())
						logger.debug("Skipping invalid origin of lemma " + 
						       lemma1.getValue() + "in language " + language1);
					continue;
				}
				
				boolean addCognate = false;
				for (Origin origin2 : lemma2.getOrigin()) {
					
					String etymology2 = origin2.getLanguage();
					String value2 = origin2.getValue();

					if (etymology2 == null || Strings.isNullOrEmpty(etymology2) ||
						value2 == null || Strings.isNullOrEmpty(value2)) {
						if (logger.isDebugEnabled())
							logger.debug("Skipping invalid origin of lemma " + 
							       lemma2.getValue() + "in language " + language2);
						continue;
					}
					
					if (etymology1.equals(etymology2)) {
						try {
							addCognate = StringUtils.areEqual(
										 value2, value1, relax, true);
							if (addCognate)
								break;
						}
						catch (IllegalArgumentException e) {
							logger.debug("Skipping invalid entry");
						}
					}
				}
	
				if (addCognate) {
					try {
						out.write(line);
						out.write("\n\t" + ModelHandler.toString(lemma1));
						out.write("\n\t" + ModelHandler.toString(lemma2) + "\n");
					}
					catch (IOException e) {
						logger.error("Error while writing output file");
						return;
					}
				}
			}
		}
		
		try {
			out.close();
		} catch (IOException e) {
			logger.error("Error while closing the file reader");
		}
	}
	
	/**
	 * Builds a {@code Map} having the {@code String} values of the lemma as keys and the 
	 * {@code Lemma} object as value. 
	 * 
	 * @param lemmas the input dataset
	 * @return a map of lemmas
	 */
	private Map<String, Lemma> buildLemmasMap(Lemmas lemmas) {
		
		Map<String, Lemma> map = new HashMap<String, Lemma>();
		
		for (Lemma lemma : lemmas.getLemma()) {
			
			String key = lemma.getValue().toLowerCase();
			if (!map.containsKey(key))	{
				map.put(key, lemma);
			}
		}
		
		return map;
	}
}
