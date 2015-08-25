package ro.unibuc.nlp.cognates.etymology;

import java.util.Arrays;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;

import ro.unibuc.nlp.cognates.etymology.model.Lemmas;
import ro.unibuc.nlp.cognates.etymology.model.ModelHandler;

import com.google.common.io.Resources;

public class EtymologyUtilsTest {

	@Test
	public void testPositiveFiltering() {
		
		try {
			String path = Resources.getResource("etymology/lemmas-ok.xml").getPath();
			Lemmas lemmas = ModelHandler.readModel(path);
			
			Lemmas filtered = EtymologyUtils.getLemmasWithEtymology(lemmas, Arrays.asList("french"));
			
			Assert.assertNotNull(filtered);
			Assert.assertNotNull(filtered.getLemma());
			Assert.assertEquals(5, filtered.getLemma().size());

			filtered = EtymologyUtils.getLemmasWithEtymology(path, Arrays.asList("french", "italian"));
			
			Assert.assertNotNull(filtered);
			Assert.assertNotNull(filtered.getLemma());
			Assert.assertEquals(6, filtered.getLemma().size());
		} 
		catch (JAXBException | IllegalArgumentException e) {
			Assert.fail("Unexpected error while loading model.");
		}
	}
	
	@Test
	public void testNegativeFiltering() {
		
		try {
			String path = Resources.getResource("etymology/lemmas-ok.xml").getPath();
			Lemmas lemmas = ModelHandler.readModel(path);
			
			Lemmas filtered = EtymologyUtils.getLemmasWithoutEtymology(lemmas, Arrays.asList("spanish"));
			
			Assert.assertNotNull(filtered);
			Assert.assertNotNull(filtered.getLemma());
			Assert.assertEquals(8, filtered.getLemma().size());

			filtered = EtymologyUtils.getLemmasWithoutEtymology(path, Arrays.asList("latin", "turkish"));
			
			Assert.assertNotNull(filtered);
			Assert.assertNotNull(filtered.getLemma());
			Assert.assertEquals(6, filtered.getLemma().size());
		} 
		catch (JAXBException | IllegalArgumentException e) {
			Assert.fail("Unexpected error while loading model.");
		}
	}
}