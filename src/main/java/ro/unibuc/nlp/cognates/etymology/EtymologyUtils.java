package ro.unibuc.nlp.cognates.etymology;

import java.rmi.UnmarshalException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;

import ro.unibuc.nlp.cognates.etymology.model.Lemma;
import ro.unibuc.nlp.cognates.etymology.model.Lemmas;
import ro.unibuc.nlp.cognates.etymology.model.ModelHandler;
import ro.unibuc.nlp.cognates.etymology.model.Origin;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Provides filters for lemmas based on their etymologies.
 * 
 * @author alina
 */
public class EtymologyUtils {
	
	private static String SEPARATOR = "____";
	
	/**
	 * Filters lemmas and returns those having an etymon in at least one of the given languages.
	 * 
	 * @param lemmas the input dataset
	 * @param languages the list of languages to filter by
	 * @return a subset of the initial dataset
	 */
	public static Lemmas getLemmasWithEtymology(Lemmas lemmas, List<String> languages) {
		
		Lemmas filtered = new Lemmas();
		
		if (lemmas == null) {
			return filtered;
		}
		
		for (Lemma lemma : lemmas.getLemma()) {
			loop: 
			for (Origin origin : lemma.getOrigin()) {
				for (String language : languages) {
					
					if (language.equals(origin.getLanguage())) {
						filtered.getLemma().add(lemma);
						break loop;
					}
				}
			}
		}
		
		return filtered;
	}
	
	/**
	 * Filters lemmas and returns those having an etymon in at least one of the given languages.
	 * 
	 * @param in the file path from where to load the input dataset
	 * @param languages the list of languages to filter by
	 * @return a subset of the initial dataset
	 * @throws JAXBException
	 */
	public static Lemmas getLemmasWithEtymology(String in, List<String> languages) 
			throws JAXBException, UnmarshalException, IllegalArgumentException {
		
		Lemmas lemmas = ModelHandler.readModel(in);
		return getLemmasWithEtymology(lemmas, languages);
	}
	
	
	/**
	 * Filters lemmas and persists those having an etymon in at least one of the given languages.
	 * 
	 * @param lemmas the input dataset
	 * @param out the file path where the output subset should be persisted
	 * @param languages the list of languages to filter by
	 * @throws JAXBException
	 */
	public static void writeLemmasWithEtymology(Lemmas lemmas, String out, List<String> languages) 
			throws JAXBException, MarshalException, IllegalArgumentException {
		
		Lemmas filtered = getLemmasWithEtymology(lemmas, languages);
		ModelHandler.writeModel(filtered, out);
	}
	
	/**
	 * Filters lemmas and persists those having an etymon in at least one of the given languages.
	 * 
	 * @param in the file path from where to load the input dataset
	 * @param out the file path where the output subset should be persisted
	 * @param languages the list of languages to filter by
	 * @throws JAXBException
	 */
	public static void writeLemmasWithEtymology(String in, String out, List<String> languages) 
			throws JAXBException, MarshalException, UnmarshalException, IllegalArgumentException {
		
		Lemmas lemmas = ModelHandler.readModel(in);
		writeLemmasWithEtymology(lemmas, out, languages);
	}
	
	
	/**
	 * Filters lemmas and returns those not having an etymon in any of the given languages.
	 * 
	 * @param lemmas the input dataset
	 * @param languages the list of languages to filter by
	 * @return a subset of the initial dataset
	 */
	public static Lemmas getLemmasWithoutEtymology(Lemmas lemmas, List<String> languages) {
		
		Lemmas filtered = new Lemmas();
		
		if (lemmas == null) {
			return filtered;
		}
		
		for (Lemma lemma : lemmas.getLemma()) {
			boolean hasEtymology = false;
			
			for (Origin origin : lemma.getOrigin()) {
				for (String language : languages) {
					if (language.equals(origin.getLanguage())) {
						hasEtymology = true;
					}
				}
			}
			
//			if (lemma.getOrigin().size() > 0)
			if (!hasEtymology) {
				filtered.getLemma().add(lemma);
			}
		}
		
		return filtered;
	}
	
	/**
	 * Filters lemmas and returns those not having an etymon in any of the given languages.
	 * 
	 * @param in the file path from where to load the input dataset
	 * @param languages the list of languages to filter by
	 * @return a subset of the initial dataset
	 * @throws JAXBException
	 */
	public static Lemmas getLemmasWithoutEtymology(String in, List<String> languages) 
			throws JAXBException, UnmarshalException, IllegalArgumentException {
		
		Lemmas lemmas = ModelHandler.readModel(in);
		return getLemmasWithoutEtymology(lemmas, languages);
	}
	
	/**
	 * Filters lemmas and persists those not having an etymon in any of the given languages.
	 * 
	 * @param lemmas the input dataset
	 * @param out the file path where the output subset should be persisted
	 * @param languages list of languages to filter by
	 * @throws JAXBException
	 */
	public static void writeLemmasWithoutEtymology(Lemmas lemmas, String out, List<String> languages) 
			throws JAXBException, MarshalException, IllegalArgumentException {
		
		Lemmas filtered = getLemmasWithoutEtymology(lemmas, languages);
		ModelHandler.writeModel(filtered, out);
	}
	
	/**
	 * Filters lemmas and persists those not having an etymon in any of the given languages.
	 * 
	 * @param in the file path from where to load the input dataset
	 * @param out the file path where the output subset should be persisted
	 * @param languages the list of languages to filter by
	 * @throws JAXBException
	 */
	public static void writeLemmasWithoutEtymology(String in, String out, List<String> languages) 
			throws JAXBException, UnmarshalException, MarshalException, IllegalArgumentException {
		
		Lemmas filtered = getLemmasWithoutEtymology(in, languages);
		ModelHandler.writeModel(filtered, out);
	}
	
	
	/**
	 * Merges multiple datasets of lemmas.
	 * 
	 * @param datasets the datasets to be merged
	 * @return a merged dataset of lemmas
	 */
	public static Lemmas merge(List<Lemmas> datasets) {
		
		Multimap<String, String> map = ArrayListMultimap.create();
		
		for (Lemmas dataset: datasets) {
			addValues(map, dataset);
		}
		
		Lemmas merged = new Lemmas();
		TreeSet<String> keys = new TreeSet<String>(map.keySet());
		
		for (String key : keys) {
			Lemma lemma = new Lemma();
			lemma.setValue(key);
			
			Set<String> etymologies = new TreeSet<String>(map.get(key));
				
			for (String etymology : etymologies) {
				if (etymology.split(SEPARATOR).length != 2)
					continue; // invalid value
				
				Origin origin = new Origin();
				origin.setLanguage(etymology.split(SEPARATOR)[0]);
				origin.setValue(etymology.split(SEPARATOR)[1]);
				
				lemma.getOrigin().add(origin);
			}
			
			merged.getLemma().add(lemma);
		}
		
		return merged;
	}
	
	/**
	 * Adds lemmas and etymologies to map.
	 * 
	 * @param map the multimap containing lemmas as keys and etymologies as values
	 * @param lemmas the new lemmas to add to the map
	 */
	private static void addValues(Multimap<String, String> map, Lemmas lemmas) {
		
		for (Lemma lemma: lemmas.getLemma()) {
			for (Origin origin : lemma.getOrigin()) {
				if (!Strings.isNullOrEmpty(origin.getValue())) {
					map.put(lemma.getValue(), origin.getLanguage() + SEPARATOR + origin.getValue());
				}
			}
		}
	}
}