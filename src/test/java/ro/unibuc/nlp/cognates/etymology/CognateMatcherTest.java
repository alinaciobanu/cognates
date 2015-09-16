package ro.unibuc.nlp.cognates.etymology;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ro.unibuc.nlp.cognates.utils.FileUtils;

import com.google.common.io.Resources;

public class CognateMatcherTest {

	@Test
	public void testCognateMatching() throws IOException {
		
		String translationPath = Resources.getResource("etymology/matcher/translations.txt").getPath();
		String lemmasPath1 = Resources.getResource("etymology/matcher/lemmas-ro.xml").getPath();
		String lemmasPath2 = Resources.getResource("etymology/matcher/lemmas-it.xml").getPath(); 
		String fileName = "matched-cognates-ro-it-test.txt";
		
		CognateMatcher matcher = new CognateMatcher();
		matcher.match(translationPath, lemmasPath1, lemmasPath2, fileName, "romanian", "italian");

		List<String> lines = FileUtils.readLines(fileName);
			
		Assert.assertEquals(3, lines.size());
		Assert.assertEquals("abonament	abbonamento", lines.get(0).trim());
		Assert.assertEquals("abonament: [abonnement(french)]", lines.get(1).trim());
		Assert.assertEquals("abbonamento: [abonnement(french)]", lines.get(2).trim());
		
		// clean up
		Path path = FileSystems.getDefault().getPath(fileName);
		Files.delete(path);
	}
	
	@Test
	public void testCognateMatchingWithRelaxation() throws IOException {
		
		String translationPath = Resources.getResource("etymology/matcher/translations.txt").getPath();
		String lemmasPath1 = Resources.getResource("etymology/matcher/lemmas-ro.xml").getPath();
		String lemmasPath2 = Resources.getResource("etymology/matcher/lemmas-it.xml").getPath(); 
		String fileName = "matched-cognates-ro-it-test.txt";
		
		CognateMatcher matcher = new CognateMatcher();
		matcher.match(translationPath, lemmasPath1, lemmasPath2, fileName, "romanian", "italian", 2);

		List<String> lines = FileUtils.readLines(fileName);
			
		Assert.assertEquals(6, lines.size());
		Assert.assertEquals("abonament	abbonamento", lines.get(0).trim());
		Assert.assertEquals("abonament: [abonnement(french)]", lines.get(1).trim());
		Assert.assertEquals("abbonamento: [abonnement(french)]", lines.get(2).trim());
		Assert.assertEquals("administrator	amministratore", lines.get(3).trim());
		Assert.assertEquals("administrator: [administrateur(french) administrator(latin)]", lines.get(4).trim());
		Assert.assertEquals("amministratore: [administratore(latin)]", lines.get(5).trim());
		
		// clean up
		Path path = FileSystems.getDefault().getPath(fileName);
		Files.delete(path);
	}
}
