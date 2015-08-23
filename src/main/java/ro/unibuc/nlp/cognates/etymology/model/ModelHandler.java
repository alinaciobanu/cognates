package ro.unibuc.nlp.cognates.etymology.model;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;

import com.google.common.base.Strings;

/**
 * Handles model persistence and loading.
 * 
 * @author alina
 */
public class ModelHandler 
{
	/**
	 * Loads a model from a file
	 * 
	 * @param path the location of the model file
	 * @return the loaded model
	 * @throws JAXBException
	 */
	public static Lemmas readModel(String path) 
			throws JAXBException, UnmarshalException, IllegalArgumentException {
		
		if (Strings.isNullOrEmpty(path))
			throw new IllegalArgumentException("Invalid path argument.");
		
		File file = new File(path);
		JAXBContext context = JAXBContext.newInstance(Lemmas.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Lemmas lemmas = (Lemmas) unmarshaller.unmarshal(file);
			
		return lemmas;
	}

	/**
	 * Persists the model to a file
	 * 
	 * @param lemmas the model
	 * @param path the location where the model should be persisted
	 * @throws JAXBException
	 */
	public static void writeModel(Lemmas lemmas, String path) 
			throws JAXBException, MarshalException, IllegalArgumentException {
		
		if (Strings.isNullOrEmpty(path))
			throw new IllegalArgumentException("Invalid path argument.");
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Lemmas.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(lemmas, new File(path));
	}

	/**
	 * Displays error-related info.
	 * 
	 * @param e the exception to be handled
	 */
	public static void handleException(JAXBException e) {
		Throwable linkedEx = e.getLinkedException();
		e.printStackTrace();

		if (linkedEx != null) {
			linkedEx.printStackTrace();
		}
		else {
			System.out.println("Error while loading or persisting the model.");
		}
	}
}