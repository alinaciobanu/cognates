package ro.unibuc.nlp.cognates.utils;

import ro.unibuc.nlp.cognates.etymology.model.Lemma;
import ro.unibuc.nlp.cognates.etymology.model.Lemmas;
import ro.unibuc.nlp.cognates.etymology.model.Origin;

import com.google.common.base.Strings;

/**
 * Handles diacritics removal
 * 
 * @author alina
 */
public class DiacriticUtils {

	public static StringBuilder aString = new StringBuilder();
	public static StringBuilder eString = new StringBuilder();
	public static StringBuilder iString = new StringBuilder();
	public static StringBuilder oString = new StringBuilder();
	public static StringBuilder uString = new StringBuilder();
	public static StringBuilder tString = new StringBuilder();
	public static StringBuilder sString = new StringBuilder();
	public static StringBuilder gString = new StringBuilder();
	public static StringBuilder zString = new StringBuilder();
	public static StringBuilder rString = new StringBuilder();		
	public static StringBuilder cString = new StringBuilder();		
	public static StringBuilder nString = new StringBuilder();		
	public static StringBuilder yString = new StringBuilder();

	// TODO: complete the lists of diacritics
	static {
		aString.append("[");
		aString.append((char)259);
		aString.append((char)257);
		aString.append((char)261);
		aString.append((char)513);
		aString.append((char)228);
		aString.append((char)227);
		aString.append((char)226);
		aString.append((char)225);
		aString.append((char)224);
		aString.append("]");

		eString.append("[");
		eString.append((char)275);
		eString.append((char)277);
		eString.append((char)283);
		eString.append((char)281);
		eString.append((char)235);
		eString.append((char)234);
		eString.append((char)233);
		eString.append((char)232);
		eString.append("]");

		iString.append("[");
		iString.append((char)8145);
		iString.append((char)297);
		iString.append((char)301);
		iString.append((char)239);
		iString.append((char)238);
		iString.append((char)237);
		iString.append((char)299);
		iString.append("]");

		oString.append("[");
		oString.append((char)337);
		oString.append((char)466);
		oString.append((char)335);
		oString.append((char)333);
		oString.append((char)491);
		oString.append((char)490);
		oString.append((char)246);
		oString.append((char)244);
		oString.append((char)242);
		oString.append((char)243);
		oString.append((char)212);
		oString.append("]");

		uString.append("[");
		uString.append((char)369);
		uString.append((char)252);
		uString.append((char)250);
		uString.append((char)251);
		uString.append((char)249);
		uString.append((char)365);
		uString.append((char)363);
		uString.append((char)218);
		uString.append("]");

		tString.append("[");
		tString.append((char)539);
		tString.append((char)355);
		tString.append((char)254);
		tString.append("]");

		sString.append("[");
		sString.append((char)537);
		sString.append((char)353);
		sString.append((char)347);
		sString.append((char)351);
		sString.append((char)186);
		sString.append("]");


		gString.append("[");
		gString.append((char)287);
		gString.append("]");

		nString.append("[");
		nString.append((char)324);
		nString.append((char)241);
		nString.append("]");

		cString.append("[");
		cString.append((char)263);
		cString.append((char)269);
		cString.append((char)231);
		cString.append("]");

		rString.append("[");
		rString.append((char)7773);
		rString.append("]");

		zString.append("[");
		zString.append((char)382);
		zString.append("]");

		yString.append("[");
		yString.append((char)253);
		yString.append("]");

	}

	/**
	 * Removes the diacritics from the input string
	 * 
	 * @param string the input string
	 * @return the input string without diacritics
	 */
	public static String removeDiacritics(String string) {
		
		if (string == null)
			return string;
		
		string = string.replaceAll(aString.toString(), "a")
					   .replaceAll(eString.toString(), "e")
					   .replaceAll(iString.toString(), "i")
					   .replaceAll(oString.toString(), "o")
					   .replaceAll(uString.toString(), "u")
					   .replaceAll(tString.toString(), "t")
					   .replaceAll(sString.toString(), "s")
					   .replaceAll(gString.toString(), "g")
					   .replaceAll(nString.toString(), "n")
					   .replaceAll(cString.toString(), "c")
					   .replaceAll(rString.toString(), "r")
					   .replaceAll(yString.toString(), "y")
					   .replaceAll(zString.toString(), "z")
					   .replaceAll(aString.toString().toUpperCase(), "A")
					   .replaceAll(eString.toString().toUpperCase(), "E")
					   .replaceAll(iString.toString().toUpperCase(), "I")
					   .replaceAll(oString.toString().toUpperCase(), "O")
					   .replaceAll(uString.toString().toUpperCase(), "U")
					   .replaceAll(tString.toString().toUpperCase(), "T")
					   .replaceAll(sString.toString().toUpperCase(), "S")
					   .replaceAll(gString.toString().toUpperCase(), "G")
					   .replaceAll(nString.toString().toUpperCase(), "N")
					   .replaceAll(cString.toString().toUpperCase(), "C")
					   .replaceAll(rString.toString().toUpperCase(), "R")
					   .replaceAll(yString.toString().toUpperCase(), "Y")
					   .replaceAll(zString.toString().toUpperCase(), "Z");

		return string;
	}

	/**
	 * Removes the diacritics from the input dataset of lemmas, for each lemma and its origins.
	 * 
	 * @param lemmas the input dataset
	 */
	public static void removeAllDiacritics(Lemmas lemmas) {
		
		for (Lemma lemma : lemmas.getLemma()) {
			lemma.setValue(removeDiacritics(Strings.nullToEmpty(lemma.getValue())));
		
			for (Origin origin : lemma.getOrigin()) {
				origin.setValue(removeDiacritics(origin.getValue()));
			}
		}
	}
}