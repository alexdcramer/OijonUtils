package net.oijon.utils.parser.data;

import java.util.ArrayList;
import java.util.Date;

import net.oijon.utils.logger.Log;
import net.oijon.utils.parser.Parser;

//last edit: 8/14/23 -N3

/**
 * The words and meaning of a language
 * @author alex
 *
 */
public class Lexicon {

	private ArrayList<Word> wordList = new ArrayList<Word>();
	
	static Log log = Parser.getLog();
	
	/**
	 * Creates an empty lexicon.
	 */
	public Lexicon() {
		
	}
	
	/**
	 * Creates a lexicon from an ArrayList of words
	 * @param words The ArrayList of words to use.
	 */
	public Lexicon(ArrayList<Word> words) {
		for (int i = 0; i < words.size(); i++) {
			this.addWord(words.get(i));
		}
	}
	
	/**
	 * Copy constructor
	 * @param l The lexicon to copy
	 */
	public Lexicon(Lexicon l) {
		for (int i = 0; i < l.wordList.size(); i++) {
			this.addWord(l.getWord(i));
		}
	}
	
	/**
	 * Adds a word to the lexicon
	 * @param word The word to be added
	 */
	public void addWord(Word word) {
		wordList.add(word);
		checkSynonyms();
		checkHomonyms();
	} 
	
	/**
	 * Removes a word from the lexicon
	 * @param word The word to be removed
	 */
	public void removeWord(Word word) {
		for (int i = 0; i < wordList.size(); i++) {
			Word checkWord = wordList.get(i);
			if (checkWord.getName().equals(word.getName()) & checkWord.getMeaning().equals(word.getMeaning())) {
				wordList.remove(i);
			}
		}
	}
	
	/**
	 * Gets the amount of words in the lexicon
	 * @return The amount of words in the lexicon
	 */
	public int size() {
		return wordList.size();
	}
	
	/**
	 * Gets a word in a lexicon via index number
	 * @param i The index number to use
	 * @return The word at position i
	 */
	public Word getWord(int i) {
		return wordList.get(i);
	}
	
	/**
	 * Checks for synonyms inside the lexicon, and marks them as such.
	 */
	public void checkSynonyms() {
		for (int i = 0; i < wordList.size(); i++) {
			for (int j = 0; j < wordList.size(); j++) {
				if (i != j) {
					if (wordList.get(i).getMeaning().equals(wordList.get(j).getMeaning())) {
						wordList.get(i).addSynonym(wordList.get(j));
					}
				}
			}
		}
	}
	
	/**
	 * Checks for homonyms inside the lexicon, and marks them as such.
	 */
	public void checkHomonyms() {
		for (int i = 0; i < wordList.size(); i++) {
			for (int j = 0; j < wordList.size(); j++) {
				if (i != j) {
					if (wordList.get(i).getName().equals(wordList.get(j).getName())) {
						wordList.get(i).addHomonym(wordList.get(j));
					}
				}
			}
		}
	}
	
	/**
	 * Parses a Lexicon from a multitag.
	 * 99% of the time, you want to use {@link net.oijon.utils.parser.Parser#parseLexicon()} instead
	 * @param docTag The multitag containing the entire .language file
	 * @return The lexicon contained in the multitag
	 */
	public static Lexicon parse(Multitag docTag) {
		try {
			Lexicon lexicon = new Lexicon();
			Multitag lexiconTag = docTag.getMultitag("Lexicon");
			ArrayList<Multitag> wordList = lexiconTag.getSubMultitags();
			for (int i = 0; i < wordList.size(); i++) {
				if (wordList.get(i).getName().equals("Word")) {
					Multitag wordTag = wordList.get(i);
					Tag valueTag = wordTag.getDirectChild("wordname");
					Tag meaningTag = wordTag.getDirectChild("meaning");
					Word word = new Word(valueTag.value(), meaningTag.value());
					try {
						Tag pronunciationTag = wordTag.getDirectChild("pronunciation");
						word.setPronounciation(pronunciationTag.value());
						Tag etymologyTag = wordTag.getDirectChild("etymology");
						word.setEtymology(etymologyTag.value());
						//TODO: Attempt to find ID of source language in Susquehanna folder. If not found, revert to null.
						//Tag sourceLanguageTag = wordTag.getDirectChild("sourceLanguage");
						//word.setSourceLanguage(null);
						Tag creationDateTag = wordTag.getDirectChild("creationDate");
						word.setCreationDate(new Date(Long.parseLong(creationDateTag.value())));
						Tag editDateTag = wordTag.getDirectChild("editDate");
						word.setEditDate(new Date(Long.parseLong(editDateTag.value())));
					} catch (Exception e) {
						log.warn("Could not find optional property for " + valueTag.value() + 
								" (" + valueTag.getName() + "). Was this word added manually?");
					}
					lexicon.addWord(word);
				}
			}
			return lexicon;
		} catch (Exception e) {
			log.err("No lexicon found! Has one been created? Returning a blank lexicon...");
			return new Lexicon();
		}
	}
	
	
	@Override
	public String toString() {
		String returnString = "===Lexicon Start===\n";
		for (int i = 0; i < wordList.size(); i++) {
			returnString += wordList.get(i).toString() + "\n";
		}
		returnString += "===Lexicon End===";
		return returnString;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Lexicon) {
			Lexicon l = (Lexicon) obj;
			if (wordList.equals(l.wordList)) {
				return true;
			}
		}
		return false;
	}
}
