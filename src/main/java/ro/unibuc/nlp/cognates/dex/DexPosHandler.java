package ro.unibuc.nlp.cognates.dex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import ro.unibuc.nlp.cognates.utils.FileUtils;

public class DexPosHandler {
	
	private Connection conn;
		
	public DexPosHandler(String url, String user, String password) 
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, user, password);
	}
	
	public Connection getConn() {
		
		return conn;
	}

	public void setConn(Connection conn) {
		
		this.conn = conn;
	}
	
	/**
	 * Extracts all the inflected forms provided by dexonline.
	 * 
	 * @param outPath: output file in which the words are written
	 * @throws SQLException
	 * @throws IOException
	 */
	public void writeAllInflectedForms(String outPath) throws SQLException, IOException {
		
		String query = "SELECT DISTINCT(LOWER(TRIM(formNoAccent))) FROM dex.inflectedform";
		
		Statement statement = getConn().createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		BufferedWriter out = FileUtils.getWriter(outPath);
	
		while (resultSet.next()) { 
			out.write(resultSet.getObject(1).toString() + "\n");
		}
			
		out.close();
		resultSet.close();
	}
	
	/**
	 * Writes all the possible lemmas and parts of speech for a list of words.
	 * 
	 * Sample input:
	 * 
	 * ai
	 * abandonați
	 * carte
	 * dânșilor
	 * poartă
	 * 
	 * Sample output:
	 * 
	 * ai: [ai(substantiv), al(pronume), avea(verb)]
	 * abandonați: [abandona(verb), abandonat(adjectiv)]
	 * carte: [carte(substantiv), cartă(substantiv)]
	 * dânșilor: [dânsul(pronume)]
	 * poartă: [poartă(substantiv), purta(verb)]
	 * 
	 * @param inPath: input file in which words are written each on a line
	 * @param outPath: output file in which words are written together with all their possible lemmas and parts of speech
	 * @throws IOException
	 * @throws SqlException
	 */
	public void writeLemmasAndPos(String inPath, String outPath) throws IOException, SQLException {
		
		BufferedReader in = FileUtils.getReader(inPath);
		BufferedWriter out = FileUtils.getWriter(outPath);

		String word;
		while ((word = in.readLine()) != null) {	
			Set<String> posSet = getLemmasAndPos(word);
			out.write(word + ": [" + getConcatenation(posSet) + "]\n");
		}

		in.close();
		out.close();
	}
	
	/**
	 * Retrieves all the possible lemmas and parts of speech for a word.
	 * 
	 * Sample input:
	 * 
	 * ai
	 * 
	 * Sample output:
	 * 
	 * [ai(substantiv), al(pronume), avea(verb)]
	 * 
	 * @param word the input word
	 * @return a set containing all the possible lemmas and parts of speech for the input word 
	 * @throws SQLException
	 */
	public Set<String> getLemmasAndPos(String word) throws SQLException {
		
		Set<String> posSet = new TreeSet<String>();
		
		Statement statement = getConn().createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT DISTINCT(LOWER(TRIM(l.formNoAccent))), " +
				"LOWER(TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(inf.description, ' ', 1), ',', 1))) " +
				"FROM dex.inflectedform inff, dex.lexem l, dex.inflection inf " +
				"WHERE inff.lexemId = l.id AND inff.inflectionId = inf.id AND inff.formNoAccent = \'" + word + "\';");
			
		while (resultSet.next()) {
			String lemma = resultSet.getObject(1).toString();
			String pos = resultSet.getObject(2).toString();
								
			posSet.add(lemma + "(" + pos + ")");
		}
		
		return posSet;
	}
	/**
	 * Concatenates all the possible lemmas and parts of speech for a word.
	 * 
	 * @param posSet: set of strings in which each element has the form: lemma(POS)
	 * @return the concatenated lemmas and parts of speech
	 */
	private String getConcatenation(Set<String> posSet) {
		
		String concatenation = "";
		Iterator<String> iterator = posSet.iterator();
			
		while (iterator.hasNext()) {
			concatenation += iterator.next() + ", ";
		}
		
		// remove final comma and space
		return concatenation.substring(0, concatenation.length() - 2);
	}
}