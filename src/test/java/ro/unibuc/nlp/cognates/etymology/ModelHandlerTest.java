package ro.unibuc.nlp.cognates.etymology;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;

import org.junit.Assert;
import org.junit.Test;

import ro.unibuc.nlp.cognates.etymology.model.Lemma;
import ro.unibuc.nlp.cognates.etymology.model.Lemmas;
import ro.unibuc.nlp.cognates.etymology.model.ModelHandler;
import ro.unibuc.nlp.cognates.etymology.model.Origin;

import com.google.common.io.Resources;

public class ModelHandlerTest {

	@Test
	public void readModelTest() {
		
		try {
			String path = Resources.getResource("etymology/lemmas-ok.xml").getPath();
			Lemmas lemmas = ModelHandler.readModel(path);
			
			Assert.assertNotNull(lemmas);
			Assert.assertNotNull(lemmas.getLemma());
			Assert.assertEquals(10, lemmas.getLemma().size());
			
			List<Integer> etymologies = Arrays.asList(1, 1, 2, 1, 2, 1, 2, 2, 1, 2);
			for (int i = 0; i < etymologies.size(); i++) {
				Assert.assertEquals((int)etymologies.get(i), lemmas.getLemma().get(i).getOrigin().size());
			}
		} 
		catch (JAXBException | IllegalArgumentException e) {
			Assert.fail("Unexpected error while loading model.");
		}
	}
	
	@Test
	public void readEmptyModelTest() {
		
		try {
			String path = Resources.getResource("etymology/lemmas-empty.xml").getPath();
			Lemmas lemmas = ModelHandler.readModel(path);
			
			Assert.assertNotNull(lemmas);
			Assert.assertNotNull(lemmas.getLemma());
			Assert.assertEquals(0, lemmas.getLemma().size());
		} 
		catch (JAXBException  | IllegalArgumentException e) {
			Assert.fail("Unexpected error while loading model.");
		}
	}
	
	@Test
	public void readNullModelTest() {
		
		try {
			ModelHandler.readModel("");
			Assert.fail("Error expected");
		} 
		catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
		
		try {
			ModelHandler.readModel(null);
			Assert.fail("Error expected");
		} 
		catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
	}

	@Test
	public void readIncorrectFormatTest() {
		
		try {
			String path = Resources.getResource("etymology/lemmas-bad-format.xml").getPath();
			ModelHandler.readModel(path);
			Assert.fail("Error expected");
		} 
		catch (Exception e) {
			Assert.assertTrue(e instanceof JAXBException);
		}
		
		try {
			String path = Resources.getResource("etymology/lemmas-other-format.xml").getPath();
			ModelHandler.readModel(path);
			Assert.fail("Error expected");
		} 
		catch (Exception e) {
			Assert.assertTrue(e instanceof JAXBException);
		}
		
		try {
			ModelHandler.readModel("dummy-file.xml");
			Assert.fail("Error expected");
		} 
		catch (Exception e) {
			Assert.assertTrue(e instanceof UnmarshalException);
		}
	}

	@Test
	public void writeModelTest() throws IOException {
		
		String fileName = "lemmas-model-handler-test.xml";
		
		Lemmas lemmas = new Lemmas();
		
		Origin origin = new Origin();
		origin.setLanguage("language1");
		origin.setValue("value1");
		
		Lemma lemma = new Lemma();
		lemma.setValue("lemma1");
		lemma.getOrigin().add(origin);
		
		lemmas.getLemma().add(lemma);
		
		origin = new Origin();
		origin.setLanguage("language2");
		origin.setValue("value2");
		
		lemma = new Lemma();
		lemma.setValue("lemma2");
		lemma.getOrigin().add(origin);
		
		origin = new Origin();
		origin.setLanguage("language3");
		origin.setValue("value3");
		
		lemma.getOrigin().add(origin);
		
		lemmas.getLemma().add(lemma);
		
		try {
			ModelHandler.writeModel(lemmas, fileName);
			lemmas = ModelHandler.readModel(fileName);
			
			Assert.assertNotNull(lemmas);
			Assert.assertNotNull(lemmas.getLemma());
			Assert.assertEquals(2, lemmas.getLemma().size());
			
			List<Integer> etymologies = Arrays.asList(1, 2);
			for (int i = 0; i < etymologies.size(); i++) {
				Assert.assertEquals((int)etymologies.get(i), lemmas.getLemma().get(i).getOrigin().size());
			}
		}
		catch (JAXBException e) {
			Assert.fail("Unexpected error while loading model: " + e.getStackTrace().toString());
		}
		
		// clean up
		Path path = FileSystems.getDefault().getPath(fileName);
		Files.delete(path);
	}


	@Test
	public void writeEmptyModelTest() throws IOException {
		
		String fileName = "lemmas-model-handler-test.xml";
		Lemmas lemmas = new Lemmas();

		try {
			ModelHandler.writeModel(lemmas, fileName);
			lemmas = ModelHandler.readModel(fileName);
			
			Assert.assertNotNull(lemmas);
			Assert.assertNotNull(lemmas.getLemma());
			Assert.assertEquals(0, lemmas.getLemma().size());
		}
		catch (JAXBException e) {
			Assert.fail("Unexpected error while loading model: " + e.getStackTrace().toString());
		}
		
		// clean up
		Path path = FileSystems.getDefault().getPath(fileName);
		Files.delete(path);
	}
	
	@Test
	public void writeNullModelTest() throws IOException {
		
		String fileName = "lemmas-model-handler-test.xml";
		Lemmas lemmas = null;

		try {
			ModelHandler.writeModel(lemmas, fileName);
			Assert.fail("Error expected");
		}
		catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	@Test
	public void writeIncorrectModelTest() throws IOException {
		
		Lemmas lemmas = new Lemmas();

		try {
			ModelHandler.writeModel(lemmas, "");
			Assert.fail("Error expected");
		}
		catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}

		try {
			ModelHandler.writeModel(lemmas, null);
			Assert.fail("Error expected");
		}
		catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}

		try {
			ModelHandler.writeModel(lemmas, "/dummy-folder/dummy-file.xml");
			Assert.fail("Error expected");
		}
		catch (Exception e) {
			Assert.assertTrue(e instanceof JAXBException);
		}
	}
}