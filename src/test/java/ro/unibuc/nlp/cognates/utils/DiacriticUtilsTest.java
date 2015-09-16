package ro.unibuc.nlp.cognates.utils;

import org.junit.Assert;
import org.junit.Test;

import ro.unibuc.nlp.cognates.etymology.model.Lemma;
import ro.unibuc.nlp.cognates.etymology.model.Lemmas;
import ro.unibuc.nlp.cognates.etymology.model.Origin;

public class DiacriticUtilsTest {

	@Test
	public void testRemoveDiacritics() {
		
		Assert.assertEquals("aistaAISTA", DiacriticUtils.removeDiacritics("ăîșțâĂÎȘȚÂ"));
		Assert.assertEquals("aistaAISTA", DiacriticUtils.removeDiacritics("aistaAISTA"));
		Assert.assertEquals("", DiacriticUtils.removeDiacritics(""));
		Assert.assertNull(DiacriticUtils.removeDiacritics(null));
	}
	
	@Test
	public void testRemoveAllDiacritics() {
		
Lemmas lemmas = new Lemmas();
		
		Origin origin = new Origin();
		origin.setLanguage("language1ăîșțâ");
		origin.setValue("value1ăîșțâ");
		
		Lemma lemma = new Lemma();
		lemma.setValue("lemma1ăîșțâ");
		lemma.getOrigin().add(origin);
		
		lemmas.getLemma().add(lemma);
		
		origin = new Origin();
		origin.setLanguage("language2ĂÎȘȚÂ");
		origin.setValue("value2ĂÎȘȚÂ");
		
		lemma = new Lemma();
		lemma.setValue("lemma2");
		lemma.getOrigin().add(origin);
		
		origin = new Origin();
		origin.setLanguage("language3");
		origin.setValue("value3");
		
		lemma.getOrigin().add(origin);
		
		lemmas.getLemma().add(lemma);
		
		DiacriticUtils.removeAllDiacritics(lemmas);
		
		Assert.assertEquals("lemma1aista", lemmas.getLemma().get(0).getValue());
		Assert.assertEquals("value1aista", lemmas.getLemma().get(0).getOrigin().get(0).getValue());
		Assert.assertEquals("language1ăîșțâ", lemmas.getLemma().get(0).getOrigin().get(0).getLanguage());

		Assert.assertEquals("lemma2", lemmas.getLemma().get(1).getValue());
		Assert.assertEquals("value2AISTA", lemmas.getLemma().get(1).getOrigin().get(0).getValue());
		Assert.assertEquals("language2ĂÎȘȚÂ", lemmas.getLemma().get(1).getOrigin().get(0).getLanguage());
		Assert.assertEquals("value3", lemmas.getLemma().get(1).getOrigin().get(1).getValue());
		Assert.assertEquals("language3", lemmas.getLemma().get(1).getOrigin().get(1).getLanguage());
	}
}
