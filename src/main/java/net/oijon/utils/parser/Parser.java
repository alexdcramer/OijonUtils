package net.oijon.utils.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import net.oijon.utils.logger.Log;
import net.oijon.utils.parser.data.Language;
import net.oijon.utils.parser.data.Lexicon;
import net.oijon.utils.parser.data.Multitag;
import net.oijon.utils.parser.data.Orthography;
import net.oijon.utils.parser.data.PhonoCategory;
import net.oijon.utils.parser.data.PhonoSystem;
import net.oijon.utils.parser.data.PhonoTable;
import net.oijon.utils.parser.data.Phonology;
import net.oijon.utils.parser.data.Tag;
import net.oijon.utils.parser.data.Word;

//last edit: 8/14/2023 -N3

/**
 * Parses a .language file, and allows various parts to be accessed
 * @author alex
 *
 */
public class Parser {
	
	Log log = new Log(System.getProperty("user.home") + "/.oijonUtils", true);
	private Multitag tag;
	
	/**
	 * Creates an object to hold the contents of a .language structured string
	 * @param input The string to be parsed.
	 */
	public Parser(String input) {
		log.setDebug(true);
		input.replace("	", "");
		String[] splitLines = input.split("\n");
		/**
		 * New plan:
		 * Because the entire file has to be in a PHOSYS tag, just use that with parseMulti :)
		 */
		if (splitLines[0].equals("===PHOSYS Start===")) {
			parseMulti(input);
		} else {
			log.err("Input is not a valid PHOSYS file!");
		}
	}
	
	/**
	 * Creates an object to hold the contents of a .language structured file
	 * @param file The file to be read
	 */
	public Parser(File file) {
		log.setDebug(true);
		try {
			Scanner scanner = new Scanner(file);
			String wholeFile = "";
			while (scanner.hasNextLine()) {
				wholeFile += scanner.nextLine() + "\n";
			}
			String[] splitLines = wholeFile.split("\n");
			if (splitLines[0].equals("===PHOSYS Start===")) {
				parseMulti(wholeFile);
			} else {
				log.err("Input is not a valid PHOSYS file!");
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.critical("File " + file.toString() + " not found! Cannot parse.");
		}
		
	}
	
	/**
	 * Gets the containing tag, including all subtags
	 * @return
	 */
	public Multitag getPHOSYSTag() {
		return this.tag;
	}
	
	/**
	 * Checks if a line contains a multitag marker
	 * @param line The line to be checked
	 * @return true if the line is a multitag marker, false otherwise
	 */
	private boolean isMultitagMarker(String line) {
		String[] splitSpace = line.split(" ");
		String[] splitColon = line.split(":");
		if (splitSpace.length == 2 & splitColon.length != 2) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if a line contains a starting multitag marker
	 * @param line The line to be checked
	 * @return true if the line is a starting multitag marker, false otherwise
	 */
	private boolean isMultitagStart(String line) {
		String[] splitSpace = line.split(" ");
		if (isMultitagMarker(line)) {
			if (splitSpace[1].equals("Start===")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if a line contains an ending multitag marker
	 * @param line The line to be checked
	 * @return true if the line is an ending multitag marker, false otherwise
	 */
	private boolean isMultitagEnd(String line) {
		String[] splitSpace = line.split(" ");
		if (isMultitagMarker(line)) {
			if (splitSpace[1].equals("End===")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the name of a multitag from its marker
	 * @param line The line with the marker in it
	 * @return The name of the given multitag
	 */
	private String getMarkerTagName(String line) {
		if (isMultitagMarker(line)) {
			String[] splitSpace = line.split(" ");
			String name = splitSpace[0].substring(3);
			return name;
		} else {
			return "";
		}
	}
	
	/**
	 * Checks if a closing tag is the correct tag for a given tag name
	 * @param line The line to check for a closing tag
	 * @param name The name of the expected closing tag
	 * @return true if line is the expected closing tag, false if either not a closing tag or a closing tag for a different multitag
	 */
	private boolean isCloseForName(String line, String name) {
		if (isMultitagEnd(line)) {
			if (getMarkerTagName(line).equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the line provided is representative of a Tag object
	 * @param line The line to check
	 * @return true if is in the format of a Tag in string form, false otherwise
	 */
	private boolean isDataTag(String line) {
		String[] splitColon = line.split(":");
		if (splitColon.length == 2) {
			return true;
		}
		return false;
	}
	
	/**
	 * Parses a single tag from a line.
	 * @param line The line of the file to parse from
	 * @return A Tag object that the line represents
	 */
	private Tag parseSingle(String line) {
		if (isDataTag(line)) {
			String[] splitColon = line.split(":");
			// Extracts the name of the tag
			String name = splitColon[0];
			// Extracts the data of the tag
			String data = splitColon[1];
			// Creates a tag object in memory
			Tag childTag = new Tag(name, data);
			// Adds tag object to parent
			return childTag;
		} else {
			return null;
		}
	}
	
	/**
	 * Parses a multitag from a .language structured string
	 * @param input The .language structured string to be read
	 * @return A multitag object with all data inside.
	 * 
	 * A warning to whoever dares edit this: This code, when edited just slightly,
	 * likes to break in unpredictable ways. It is the definition of spaghetti code.
	 * As such, every line has been commented to explain what it does.
	 */
	private Multitag parseMulti(String input) {
		// Removes tabs from input. Planned addition, but never added...
		input = input.replace("	", "");
		// Splits each line based off line breaks
		String[] splitLines = input.split("\n");
		// Gets the tag name from the start tag. This removes the beginning '==='
		String tagName = getMarkerTagName(splitLines[0]);
		
		// This creates a new Multitag in memory named after the tag just named
		Multitag tag = new Multitag(tagName);
		// Loop over each line in file
		for (int i = 1; i < splitLines.length; i++) {
			// Checks if the line matches the pattern of a multitag marker
			if (isMultitagMarker(splitLines[i])) {
				// Checks if said line is a start marker
				if (isMultitagStart(splitLines[i])) {
					// Gets the name of the start marker
					String name = getMarkerTagName(splitLines[i]);
					// Gets the line number the tag was found on
					int lineNum = i + 1;
					// Creates a variable for the loop below
					String tagInput = "";
					// Loops over the lines in the file, starting at the start marker
					for (int j = i; j < splitLines.length; j++) {
						// Checks if a line is not an end marker
						if (!isCloseForName(splitLines[j], name)) {
							// Adds line to tagInput if it is not an end marker
							tagInput += splitLines[j] + "\n";
						// Checks if a line is an end marker
						} else if (isCloseForName(splitLines[j], name)){
							// Starts parsing tagInput
							Multitag childTag = parseMulti(tagInput);
							// Adds the parsed multitag to the current tag
							tag.addMultitag(childTag);
							// skips to the end of the multitag
							i = j;
							// breaks the loop that reads into the multitag
							break;
						}
						// Checks if at the end of the file
						if (j == splitLines.length - 1) {
							// Logs if at end of file and close has not been found
							log.err("Tag " + name + " on line " + lineNum + " is not closed!");
						}
					}
				}
			// Checks if line is named tag
			} else if (isDataTag(splitLines[i])) {
				// Adds tag object to parent
				tag.addTag(parseSingle(splitLines[i]));
			// Checks if line is nameless (weird)
			} else if (splitLines[i] != "") {
				// Creates a tag with no name in memory
				Tag childTag = new Tag("", splitLines[i]);
				// Adds tag object to parent
				tag.addTag(childTag);
			}
		}
		// Not sure what this is doing, probably setting parent for future multitags?
		this.tag = tag;
		// Returns the parsed multitag
		return tag;
	}
	
	/**
	 * Parses a phonology system from a Parser
	 * @return A PhonoSystem object with data from the Parser.
	 * @throws Exception Thrown when a phonology system could not be found
	 */
	public PhonoSystem parsePhonoSys() throws Exception {
		try {
			Multitag tablelist = this.tag.getMultitag("Tablelist");
			Tag tablelistName = tablelist.getDirectChild("tablelistName");
			Tag diacriticList;
			try {
				diacriticList = tablelist.getDirectChild("diacriticList");
			} catch (Exception e) {
				log.warn(e.toString());
				diacriticList = new Tag("diacriticList", "");
			}
			PhonoSystem phonoSystem = new PhonoSystem(tablelistName.value());
			ArrayList<String> diacritics = new ArrayList<String>(Arrays.asList(diacriticList.value().split(",")));
			phonoSystem.setDiacritics(diacritics);
			for (int i = 0; i < tablelist.getSubMultitags().size(); i++) {
				if (tablelist.getSubMultitags().get(i).getName().equals("PhonoTable")) {
					Multitag phonoTableTag = tablelist.getSubMultitags().get(i);
					Tag tableName = phonoTableTag.getDirectChild("tableName");
					Tag columnNames = phonoTableTag.getDirectChild("columnNames");
					Tag soundsPerCell = phonoTableTag.getDirectChild("soundsPerCell");
					Tag rowNames = phonoTableTag.getDirectChild("rowNames");
					ArrayList<Tag> tableData = phonoTableTag.getUnattachedData();
					
					String name = tableName.value();
					ArrayList<String> columns = new ArrayList<String>(Arrays.asList(columnNames.value().split(",")));
					ArrayList<String> rowNamesList = new ArrayList<String>(Arrays.asList(rowNames.value().split(",")));
					int perCell = 0;
					try {
						perCell = Integer.parseInt(soundsPerCell.value());
					} catch (NumberFormatException nfe) {
						log.err("soundsPerCell must be integer in " + tableName.value());
						log.err(nfe.toString());
						throw nfe;
					}
					
					ArrayList<PhonoCategory> cats = new ArrayList<PhonoCategory>();
					for (int j = 0; j < rowNamesList.size(); j++) {
						PhonoCategory cat = new PhonoCategory(rowNamesList.get(j));
						// TODO: allow multiple character sounds?
						try {
							String catData = tableData.get(j).value();
							for (int k = 0; k < catData.length(); k++) {
								cat.addSound(Character.toString(catData.charAt(k)));
							}
							cats.add(cat);
						} catch (IndexOutOfBoundsException e) {
							log.warn("No data found in table " + name);
						}
					}
					
					PhonoTable phonoTable = new PhonoTable(name, columns, cats, perCell);
					phonoSystem.addTable(phonoTable);
				}
			}
			return phonoSystem;
		} catch (Exception e) {
			log.err(e.toString());
			throw e;
		}
	}
	/**
	 * Parses a phonology from a Parser
	 * @return A Phonology object with data from the Parser.
	 * @throws Exception Thrown when a phonology could not be found
	 */
	public Phonology parsePhono() throws Exception {
		try {
			PhonoSystem phonoSystem = this.parsePhonoSys();
			Multitag phonoTag = this.tag.getMultitag("Phonology");
			Tag soundListTag = phonoTag.getDirectChild("soundlist");
			String soundData = soundListTag.value();
			String[] soundList = soundData.split(",");
			// TODO: parse phonotactics
			Phonology phono = new Phonology(soundList, phonoSystem);
			return phono;
		} catch (Exception e) {
			e.printStackTrace();
			log.err(e.toString());
			throw e;
		}
	}
	/**
	 * Parses a language from a Parser
	 * @return A Language object with data from the Parser.
	 * @throws Exception Thrown when a language could not be found
	 */
	@SuppressWarnings("deprecation") // still not sure how im gonna handle language parents
	// perhaps each language could have an ID, and the language parent could be written
	// as {name}{date-created}{randomnum}?
	public Language parseLanguage() throws Exception {
		Phonology phono = this.parsePhono();
		Lexicon lexicon = this.parseLexicon();
		Orthography ortho = this.parseOrtho();
		Multitag meta = this.tag.getMultitag("Meta");
		Tag ver = new Tag("utilsVersion");
		try {
			ver = meta.getDirectChild("utilsVersion");
			if (!ver.value().isBlank()) {
				log.info("Language created with " + ver.value());
			}
		} catch (Exception e) {
			ver = meta.getDirectChild("susquehannaVersion");
			if (!ver.value().isBlank()) {
				log.info("Language created with " + ver.value());
			}
			log.warn("This language appears to have been created with a very early version of Oijon Utils!");
			log.warn("The susquehannaVersion tag was deprecated as of 1.2.0.");
		}
		
		Tag timeCreated = meta.getDirectChild("timeCreated");
		Tag lastEdited = meta.getDirectChild("lastEdited");
		Tag readonly = meta.getDirectChild("readonly");
		Tag name = meta.getDirectChild("name");
		Tag autonym = meta.getDirectChild("autonym");
		Tag parent = meta.getDirectChild("parent");
		Language lang = new Language(name.value());
		
		Tag id = new Tag("id");
		try {
			id = meta.getDirectChild("id");
			if (!id.value().isBlank() & !id.value().equals("null")) {
				log.info("ID of language is " + id.value());
				lang.setID(id.value());
			} else {
				log.err("This language appears to have a blank or null ID!");
				log.warn("Generating new ID, this may break relations with other languages!");
				lang.generateID();
				log.warn("New ID: " + lang.getID() + ". If other languages are related to this language, "
						+ "a manual switch to the new ID will be neccessary.");
			}
		} catch (Exception e) {
			log.warn("This language appears to have been created with a very early version of Oijon Utils!");
			log.warn("The id tag was required as of 1.2.0.");
			lang.generateID();
		}
		
		lang.setPhono(phono);
		lang.setOrtho(ortho);
		lang.setLexicon(lexicon);
		lang.setCreated(new Date(Long.parseLong(timeCreated.value())));
		lang.setEdited(new Date(Long.parseLong(lastEdited.value())));
		lang.setAutonym(autonym.value());
		lang.setReadOnly(Boolean.parseBoolean(readonly.value()));
		lang.setParent(new Language(parent.value()));
		lang.setVersion(ver.value());
		return lang;
	}
	
	public Orthography parseOrtho() {
		try {
			Orthography ortho = new Orthography();
			Multitag orthoTag = this.tag.getMultitag("Orthography");
			ArrayList<Tag> orthoPairs = orthoTag.getSubtags();
			for (int i = 0; i < orthoPairs.size(); i++) {
				ortho.add(orthoPairs.get(i).getName(), orthoPairs.get(i).value());
			}
			return ortho;
		} catch (Exception e) {
			log.err("No orthography found! Has one been created? Returning a blank orthography...");
			return new Orthography();
		}
	}
	
	/**
	 * Parses a lexicon from a Parser
	 * @return A Lexicon object with data from the Parser.
	 * @throws Exception Thrown when a lexicon could not be found
	 */
	public Lexicon parseLexicon() {
		try {
			Lexicon lexicon = new Lexicon();
			Multitag lexiconTag = this.tag.getMultitag("Lexicon");
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
						//TODO: Attempt to find name of source language in Susquehanna folder. If not found, revert to null.
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
}
