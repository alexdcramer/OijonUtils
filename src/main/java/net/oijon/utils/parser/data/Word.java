package net.oijon.utils.parser.data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

//last edit: 2/11/23 -N3

/**
 * Stores a word, including various properties about the word.
 * @author alex
 *
 */
public class Word {

	private String name;
	private String meaning;
	private String pronounciation = " ";
	private String etymology = " ";
	private Language sourceLanguage = Language.NULL;
	private ArrayList<String> classes = new ArrayList<String>();
	private Date creationDate = Date.from(Instant.now());
	private Date editDate = Date.from(Instant.now());
	private ArrayList<Word> synonyms = new ArrayList<Word>();
	private ArrayList<Word> homonyms = new ArrayList<Word>();
	
	/**
	 * Creates a word
	 * @param name The word in question
	 * @param meaning What the word means
	 */
	public Word(String name, String meaning) {
		this.name = name;
		this.meaning = meaning;
		//TODO: automatically get IPA from name via orthography
	}
	
	/**
	 * Get the textual representation of a word
	 * @return Textual representation of a word
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets how a word is written
	 * @param name The way the word is written
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the meaning of a word
	 * @return The meaning of the word
	 */
	public String getMeaning() {
		return meaning;
	}
	
	/**
	 * Sets the meaning of a word
	 * @param meaning The meaning of the word
	 */
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
	
	/**
	 * Sets how a word is pronounced
	 * @param pronounciation The way the word is pronounced.
	 */
	public void setPronounciation(String pronounciation) {
		this.pronounciation = pronounciation;
	}
	
	/**
	 * Gets the pronounciation of a word
	 * @return The pronounciation of the word
	 */
	public String getPronounciation() {
		return pronounciation;
	}
	
	/**
	 * Sets where the word comes from
	 * @param etymology Where the word comes from
	 */
	public void setEtymology(String etymology) {
		this.etymology = etymology;
	}
	
	/**
	 * Gets where the word comes from. Expect " " being returned as null.
	 * @return The etymology of the word
	 */
	public String getEtymology() {
		return etymology;
	}
	
	/**
	 * Sets the language the word came from
	 * @param sourceLanguage
	 * @deprecated as of v1.1.2, writing an entire language for each word to the file seems like a disaster waiting to happen
	 */
	@Deprecated
	public void setSourceLanguage(Language sourceLanguage) {
		sourceLanguage = this.sourceLanguage;
	}
	
	/**
	 * Gets the language the word came from
	 * @return The Language object of where the word came from
	 * @deprecated as of v1.1.2, writing an entire language for each word to the file seems like a disaster waiting to happen
	 */
	public Language getSourceLanguage() {
		return sourceLanguage;
	}
	
	/**
	 * Gets the creation date of a word.
	 * @return The datetime a word was created.
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	
	/**
	 * Sets the creation date of a word. Should only be used when reading files, not writing.
	 * @param date The datetime a word was created.
	 */
	public void setCreationDate(Date date) {
		this.creationDate = date;
	}
	
	/**
	 * Gets the last time a word was edited
	 * @return The datetime a word was last edited.
	 */
	public Date getEditDate() {
		return editDate;
	}
	
	/**
	 * Sets the last time a word was edited. Should be used whenever a word is modified.
	 * @param editDate The datetime a word was last edited.
	 */
	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}
	
	/**
	 * Adds a synonym to a word, unless it has already been added
	 * @param syn The synonym to be added
	 */
	public void addSynonym(Word syn) {
		if (!synonyms.contains(syn)) {
			synonyms.add(syn);
		}
	}
	
	/**
	 * Removes a synonym at index i
	 * @param i The synonym to be removed
	 */
	public void removeSynonym(int i) {
		synonyms.remove(i);
	}
	
	/**
	 * Clears all synonyms from a word
	 */
	public void clearSynonyms() {
		synonyms.clear();
	}
	
	/**
	 * Replaces the synonym list with a new list
	 * @param synonyms The list replacing the old list
	 */
	public void setSynonyms(ArrayList<Word> synonyms) {
		synonyms = this.synonyms;
	}
	
	/**
	 * Gets a list of all synonyms
	 * @return a list of all synonyms
	 */
	public ArrayList<Word> getSynonyms() {
		return synonyms;
	}
	
	/**
	 * Adds a homonym to a word, unless it has already been added
	 * @param syn The homonym to be added
	 */
	public void addHomonym(Word hom) {
		if (!homonyms.contains(hom)) {
			homonyms.add(hom);
		}
	}
	
	/**
	 * Removes a homonym at index i
	 * @param i The homonym to be removed
	 */
	public void removeHomonym(int i) {
		homonyms.remove(i);
	}
	
	/**
	 * Replaces the homonym list with a new list
	 * @param homonym The list replacing the old list
	 */
	public void setHomonyms(ArrayList<Word> homonyms) {
		homonyms = this.homonyms;
	}
	
	/**
	 * Gets a list of all homonyms
	 * @return a list of all homonyms
	 */
	public ArrayList<Word> getHomonyms() {
		return homonyms;
	}
	
	/**
	 * Converts a word to a string
	 * Sounds like essentially the same thing, but I assure you it is not.
	 */
	public String toString() {
		String returnString = "===Word Start===\n";
		returnString += "wordname:" + name + "\n";
		returnString += "meaning:" + meaning + "\n";
		returnString += "pronounciation:" + pronounciation + "\n";
		returnString += "etymology:" + etymology + "\n";
		returnString += "sourceLanguage:" + sourceLanguage.getName() + "\n";
		returnString += "creationDate:" + creationDate.getTime() + "\n";
		returnString += "editDate:" + editDate.getTime() + "\n";
		returnString += "===Synonym Start===\n";
		for (int i = 0; i < synonyms.size(); i++) {
			returnString += ":" + synonyms.get(i).getName() + "\n";
		}
		returnString += "===Synonym End===\n";
		returnString += "===Homonym Start===\n";
		for (int i = 0; i < homonyms.size(); i++) {
			returnString += ":" + homonyms.get(i).getMeaning() + "\n";
		}
		returnString += "===Homonym End===\n";
		returnString += "===Classes Start===\n";
		for (int i = 0; i < classes.size(); i++) {
			returnString += ":" + classes.get(i) + "\n";
		}
		returnString += "===Classes End===\n";
		returnString += "===Word End===";
		return returnString;
	}
}
