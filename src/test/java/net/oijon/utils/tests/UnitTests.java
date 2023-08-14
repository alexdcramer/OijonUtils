package net.oijon.utils.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import net.oijon.utils.info.Info;
import net.oijon.utils.logger.Log;
import net.oijon.utils.parser.Parser;
import net.oijon.utils.parser.data.Language;
import net.oijon.utils.parser.data.Orthography;

class UnitTests {
	
	Log log = new Log(System.getProperty("user.home") + "/OijonUtils");

	@Test
	void testLog() {
		File logFile = new File(log.getLogFile());
		log.info("This is a test of " + Info.getVersion());
		log.info("This will create two folders under the home directory, OijonUtils and OijonUtils2. These can be deleted after the test.");
		log.info("Your home directory is " + System.getProperty("user.home"));
		log.info("=====BEGIN LOGGER TEST=====");
		log.info("Log file at " + log.getLogFile().toString());
		log.setDebug(true);
		log.debug("This is a test of a debug message. The debug marker has been set to true.");
		log.setDebug(false);
		log.debug("This is a test of a debug message. The debug marker has been set to false.");
		log.info("This is a test of an info message.");
		log.warn("This is a test of a warning message.");
		log.err("This is a test of an error message.");
		log.critical("This is a test of a critical error message.");
		log.info("======END LOGGER TEST======");
		
		try {
			Scanner sc = new Scanner(logFile);
			ArrayList<String> lines = new ArrayList<String>();
			while(sc.hasNextLine()) {
				lines.add(sc.nextLine());
			}
			sc.close();
			
			assertTrue(lines.size() > 0);
			
			for (int i = 0; i < lines.size(); i++) {
				assertNotEquals("This is a test of a debug message. The debug marker has been set to false.", lines.get(i));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(); // failing here as this means the getLogFile() method has failed.
		}
	}
	
	@Test
	void testLanguage() {
		log.info("====BEGIN LANGUAGE TEST====");
		log.info("Parsing testish.language...");
		try {
			log.setDebug(true);
			Parser parser = new Parser(Paths.get(UnitTests.class.getClassLoader().getResource("testish.language").toURI()).toFile());
			Language testLang = parser.parseLanguage();
			testLang.toFile(new File(System.getProperty("user.home") + "/OijonUtils/testish.language"));
			Parser parser2 = new Parser(new File(System.getProperty("user.home") + "/OijonUtils/testish.language"));
			Language compareLanguage = parser2.parseLanguage();
			testLang.setOrtho(compareLanguage.getOrtho()); // ortho not created in testlang, is in comparelang
			assertEquals(testLang, compareLanguage);
		} catch (Exception e) {
			e.printStackTrace();
			log.err(e.toString());
			fail();
		}
		log.info("=====END LANGUAGE TEST=====");
	}
	
	@Test
	void testOrthography() {
		log.info("===BEGIN ORTHOGRAPHY TEST===");
		log.setDebug(true);
		try {
			log.info("Parsing testish.language...");
			log.info("Expect an error about no orthography existing, this test creates it.");
			Parser parser = new Parser(Paths.get(UnitTests.class.getClassLoader().getResource("testish.language").toURI()).toFile());
			Language testLang = parser.parseLanguage();
			Orthography testOrtho = testLang.getOrtho();
			// sourced via wikipedia
			// big block here i know, this is meant to be done by user input and not manually like this
			testOrtho.add("b", "b");
			testOrtho.add("d", "d");
			testOrtho.add("dj", "d");
			testOrtho.add("dʒ", "j");
			testOrtho.add("ð", "th");
			testOrtho.add("f", "f");
			testOrtho.add("g", "g");
			testOrtho.add("h", "h");
			testOrtho.add("hw", "wh");
			testOrtho.add("j", "y");
			testOrtho.add("k", "k");
			testOrtho.add("k", "c");
			testOrtho.add("l", "l");
			testOrtho.add("lj", "l");
			testOrtho.add("m", "m");
			testOrtho.add("n", "n");
			testOrtho.add("nj", "n");
			testOrtho.add("ŋ", "ng");
			testOrtho.add("p", "p");
			testOrtho.add("ɹ", "r");
			testOrtho.add("s", "s");
			testOrtho.add("s", "c");
			testOrtho.add("sj", "s");
			testOrtho.add("ʃ", "sh");
			testOrtho.add("t", "t");
			testOrtho.add("tj", "t");
			testOrtho.add("tʃ", "ch");
			testOrtho.add("tʃ", "tch");
			testOrtho.add("θ", "th");
			testOrtho.add("θj", "th");
			testOrtho.add("v", "v");
			testOrtho.add("w", "w");
			testOrtho.add("z", "z");
			testOrtho.add("zj", "z");
			testOrtho.add("ʒ", "s");
			log.info("Testish consonants added!");
			testOrtho.add("ɑː", "a");
			testOrtho.add("ɒ", "o");
			testOrtho.add("æ", "a");
			testOrtho.add("aɪ", "i");
			testOrtho.add("aɪ", "ie");
			testOrtho.add("aʊ", "ou");
			testOrtho.add("aʊ", "ow");
			testOrtho.add("ɛ", "e");
			testOrtho.add("eɪ", "a");
			testOrtho.add("ɪ", "i");
			testOrtho.add("iː", "ee");
			testOrtho.add("iː", "e");
			testOrtho.add("oʊ", "oa");
			testOrtho.add("ɔː", "ough");
			testOrtho.add("ɔː", "au");
			testOrtho.add("ɔː", "augh");
			testOrtho.add("ɔɪ", "oi");
			testOrtho.add("ʊ", "oo");
			testOrtho.add("uː", "oo");
			testOrtho.add("uː", "u");
			testOrtho.add("ʌ", "u");
			testOrtho.add("ɑːɹ", "ar");
			testOrtho.add("ɒɹ", "or");
			testOrtho.add("æɹ", "arr");
			testOrtho.add("aɪəɹ", "ire");
			testOrtho.add("aʊəɹ", "our");
			testOrtho.add("ɛɹ", "err");
			testOrtho.add("ɛəɹ", "are");
			testOrtho.add("ɛəɹ", "ar");
			testOrtho.add("ɪɹ", "irr");
			testOrtho.add("ɪɹ", "ir");
			testOrtho.add("ɪəɹ", "ear");
			testOrtho.add("ɪəɹ", "er");
			testOrtho.add("ɔːɹ", "or");
			testOrtho.add("ɔːɹ", "oar");
			testOrtho.add("ɔɪəɹ", "oir");
			testOrtho.add("ʊɹ", "our");
			testOrtho.add("ʊəɹ", "our");
			testOrtho.add("ʊəɹ", "ure");
			testOrtho.add("ɜːɹ", "ur");
			testOrtho.add("ɜːɹ", "urr");
			testOrtho.add("ɜːɹ", "or");
			testOrtho.add("ʌɹ", "urr");
			testOrtho.add("ə", "a");
			testOrtho.add("ə", "o");
			testOrtho.add("ɪ", "i");
			testOrtho.add("i", "i");
			testOrtho.add("i", "y");
			testOrtho.add("u", "u");
			testOrtho.add("əɹ", "er");
			testOrtho.add("əɹ", "ar");
			testOrtho.add("əɹ", "or");
			testOrtho.add("oʊ", "o");
			testOrtho.add("oʊ", "ow");
			testOrtho.add("iə", "ia");
			testOrtho.add("uə", "ue");
			testOrtho.add("əl", "le");
			log.info("Added vowels!");
			testLang.setOrtho(testOrtho);
			log.info("Printing language...");
			log.info(testLang.toString());
			String expectedOrtho = "===Orthography Start===\n"
					+ "ɔː:ough\n"
					+ "ɔː:augh\n"
					+ "tʃ:tch\n"
					+ "æɹ:arr\n"
					+ "aɪəɹ:ire\n"
					+ "aʊəɹ:our\n"
					+ "ɛɹ:err\n"
					+ "ɛəɹ:are\n"
					+ "ɪɹ:irr\n"
					+ "ɪəɹ:ear\n"
					+ "ɔːɹ:oar\n"
					+ "ɔɪəɹ:oir\n"
					+ "ʊɹ:our\n"
					+ "ʊəɹ:our\n"
					+ "ʊəɹ:ure\n"
					+ "ɜːɹ:urr\n"
					+ "ʌɹ:urr\n"
					+ "ð:th\n"
					+ "hw:wh\n"
					+ "ŋ:ng\n"
					+ "ʃ:sh\n"
					+ "tʃ:ch\n"
					+ "θ:th\n"
					+ "θj:th\n"
					+ "aɪ:ie\n"
					+ "aʊ:ou\n"
					+ "aʊ:ow\n"
					+ "iː:ee\n"
					+ "oʊ:oa\n"
					+ "ɔː:au\n"
					+ "ɔɪ:oi\n"
					+ "ʊ:oo\n"
					+ "uː:oo\n"
					+ "ɑːɹ:ar\n"
					+ "ɒɹ:or\n"
					+ "ɛəɹ:ar\n"
					+ "ɪɹ:ir\n"
					+ "ɪəɹ:er\n"
					+ "ɔːɹ:or\n"
					+ "ɜːɹ:ur\n"
					+ "ɜːɹ:or\n"
					+ "əɹ:er\n"
					+ "əɹ:ar\n"
					+ "əɹ:or\n"
					+ "oʊ:ow\n"
					+ "iə:ia\n"
					+ "uə:ue\n"
					+ "əl:le\n"
					+ "b:b\n"
					+ "d:d\n"
					+ "dj:d\n"
					+ "dʒ:j\n"
					+ "f:f\n"
					+ "g:g\n"
					+ "h:h\n"
					+ "j:y\n"
					+ "k:k\n"
					+ "k:c\n"
					+ "l:l\n"
					+ "lj:l\n"
					+ "m:m\n"
					+ "n:n\n"
					+ "nj:n\n"
					+ "p:p\n"
					+ "ɹ:r\n"
					+ "s:s\n"
					+ "s:c\n"
					+ "sj:s\n"
					+ "t:t\n"
					+ "tj:t\n"
					+ "v:v\n"
					+ "w:w\n"
					+ "z:z\n"
					+ "zj:z\n"
					+ "ʒ:s\n"
					+ "ɑː:a\n"
					+ "ɒ:o\n"
					+ "æ:a\n"
					+ "aɪ:i\n"
					+ "ɛ:e\n"
					+ "eɪ:a\n"
					+ "ɪ:i\n"
					+ "iː:e\n"
					+ "uː:u\n"
					+ "ʌ:u\n"
					+ "ə:a\n"
					+ "ə:o\n"
					+ "ɪ:i\n"
					+ "i:i\n"
					+ "i:y\n"
					+ "u:u\n"
					+ "oʊ:o\n"
					+ "===Orthography End===";
			assertEquals(testOrtho.toString(), expectedOrtho);
			log.info("Parsing new language...");
			testLang.toFile(new File(System.getProperty("user.home") + "/OijonUtils/testish2.language"));
			Parser newparser = new Parser(new File(System.getProperty("user.home") + "/OijonUtils/testish2.language"));
			Language newLang = newparser.parseLanguage();
			log.info(newLang.toString());
			assertEquals(testOrtho, newLang.getOrtho());
			
			assertEquals(testOrtho.phonoGuess("ough"), "ɔː");
			assertEquals(testOrtho.orthoGuess("jutɪlz"), "yutylz");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			log.err(e.toString());
		}
		log.info("====END ORTHOGRAPHY TEST====");
	}
	
	@Test
	void testNullIDCatch() {
		try {
			Parser parser = new Parser(Paths.get(UnitTests.class.getClassLoader().getResource("testish.language").toURI()).toFile());
			Language testLang = parser.parseLanguage();
			
			log.debug("Old ID: " + testLang.getID());
			assertFalse(testLang.getID().equals("null"));
			
			testLang.setID("null");
			log.debug("Null ID: " + testLang.getID());
			assertTrue(testLang.getID().equals("null"));
			
			testLang.toFile(new File(System.getProperty("user.home") + "/OijonUtils/testish2.language"));
			
			Parser newparser = new Parser(new File(System.getProperty("user.home") + "/OijonUtils/testish2.language"));
			Language testLang2 = newparser.parseLanguage();
			
			log.debug("New ID: " + testLang2.getID());
			assertFalse(testLang2.getID().equals("null"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	void testOrthoWithApostrophe() {
		try {
			Parser parser = new Parser(Paths.get(UnitTests.class.getClassLoader().getResource("testish.language").toURI()).toFile());
			Language testLang = parser.parseLanguage();
			Orthography testOrtho = testLang.getOrtho();
			testOrtho.add("m", "m");
			testOrtho.add("n", "n");
			testOrtho.add("ŋ", "ń");
			testOrtho.add("mʲ", "m'");
			testOrtho.add("nʲ", "n'");
			testOrtho.add("ŋʲ", "ń'");
			
			testLang.toFile(new File(System.getProperty("user.home") + "/OijonUtils/testish-orthoapostrophe.language"));
			Parser newparser = new Parser(new File(System.getProperty("user.home") + "/OijonUtils/testish-orthoapostrophe.language"));
			Language testLang2 = newparser.parseLanguage();
			Orthography testOrtho2 = testLang2.getOrtho();
			
			assertEquals(testOrtho2.orthoGuess("ŋʲmnʲnmʲŋ"), "ń'mn'nm'ń");
			assertEquals(testOrtho2.phonoGuess("ń'mn'nm'ń"), "ŋʲmnʲnmʲŋ");
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
