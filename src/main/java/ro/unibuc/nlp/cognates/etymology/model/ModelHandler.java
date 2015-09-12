package ro.unibuc.nlp.cognates.etymology.model;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;

/**
 * Handles model persistence and loading.
 * 
 * @author alina
 */
public class ModelHandler 
{
	private static final Logger logger = Logger.getLogger(ModelHandler.class);
	
	/**
	 * Loads a model from a file
	 * 
	 * @param path the location of the model file
	 * @return the loaded model
	 * @throws JAXBException
	 */
	public static Lemmas readModel(String path) 
			throws JAXBException, UnmarshalException, IllegalArgumentException {
		
		if (Strings.isNullOrEmpty(path)) {
			String message = "Invalid path argument: " + path;
			logger.error(message);
			throw new IllegalArgumentException(message);
		}
		
		logger.info("Loading model from file " + path);
		
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
		
		if (Strings.isNullOrEmpty(path)) {
			String message = "Invalid path argument: " + path;
			logger.error(message);
			throw new IllegalArgumentException(message);
		}
		
		logger.info("Persisting model to file " + path);
		
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
		
		logger.error("Error while loading or persisting the model", e);

		Throwable linkedEx = e.getLinkedException();
		if (linkedEx != null) {
			logger.error(linkedEx);
		}
	}
}