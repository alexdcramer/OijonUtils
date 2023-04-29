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

//last edit: 2/11/2023 -N3

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
			log.debug(wholeFile);
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
	 * Parses a multitag from a .language structured string
	 * @param input The .language structured string to be read
	 * @return A multitag object with all data inside.
	 */
	private Multitag parseMulti(String input) {
		input = input.replace("	", "");
		String[] splitLines = input.split("\n");
		String[] splitSpace = splitLines[0].split(" ");
		String tagName = splitSpace[0].substring(3);
		
		Multitag tag = new Multitag(tagName);
		for (int i = 1; i < splitLines.length; i++) {
			splitSpace = splitLines[i].split(" ");
			String[] splitColon = splitLines[i].split(":");
			if (splitSpace.length == 2 & splitColon.length != 2) {
				if (splitSpace[1].equals("Start===")) {
					String name = splitSpace[0].substring(3);
					int lineNum = i + 1;
					String tagInput = "";
					for (int j = i; j < splitLines.length; j++) {
						if (!splitLines[j].equals("===" + name + " End===")) {
							tagInput += splitLines[j] + "\n";
						} else if (splitLines[j].equals("===" + name + " End===")){
							Multitag childTag = parseMulti(tagInput);
							log.debug("Adding child multitag " + name + " to tag " + tag.getName());
							tag.addMultitag(childTag);
							i = j;
							break;
						}
						if (j == splitLines.length - 1) {
							log.err("Tag " + name + " on line " + lineNum + " is not closed!");
						}
					}
				}
			} else if (splitColon.length == 2) {
				String name = splitColon[0];
				String data = splitColon[1];
				log.debug("Adding child tag " + name + " with data " + data + " to tag " + tag.getName());
				Tag childTag = new Tag(name, data);
				tag.addTag(childTag);
			} else if (splitLines[i] != "") {
				String data = splitLines[i];
				Tag childTag = new Tag("", data);
				tag.addTag(childTag);
			}
		}
		this.tag = tag;
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
					log.debug("Data for table " + tableName.value() + ":");
					log.debug(tableData.toString());
					
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
			if (!id.value().isBlank()) {
				log.info("ID of language is " + id.value());
			}
			lang.setID(id.value());
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
				log.debug("Added pair " + orthoPairs.get(i).getName() + ":" + orthoPairs.get(i).value());
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
						log.warn("Could not find optional property for " + valueTag.value() + ". Was this word added manually?");
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