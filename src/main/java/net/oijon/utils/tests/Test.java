package net.oijon.utils.tests;

import java.io.File;
import java.nio.file.Paths;

import net.oijon.utils.info.Info;
import net.oijon.utils.logger.Log;
import net.oijon.utils.parser.Parser;
import net.oijon.utils.parser.data.Language;

public class Test {
	
	public static void main(String args[]) {
		Log log = new Log(System.getProperty("user.home") + "/OijonUtils");
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
		/**
		log.info("=====BEGIN READER TEST=====");
		log.info("Creating new log...");
		Log log2 = new Log(System.getProperty("user.home") + "/OijonUtils2");
		log2.debug("This is a test of a debug message.");
		log2.info("This is a test of an info message.");
		log2.warn("This is a test of a warning message.");
		log2.err("This is a test of an error message.");
		log2.critical("This is a test of a critical error message.");
		log.info("Test log created at " + log.getLogFile().toString());
		log.info("Reading " + log2.getLogFile().toString() + " into " + log.getLogFile().toString() + "...");
		logger.Reader.readLog(log2.getLogFile().toString(), log.getLogFile().toString());
		log.info("======END READER TEST======");
		*/
		log.info("====BEGIN LANGUAGE TEST====");
		log.info("Parsing testish.language...");
		try {
			log.setDebug(true);
			Parser parser = new Parser(Paths.get(Test.class.getClassLoader().getResource("testish.language").toURI()).toFile());
			Language testLang = parser.parseLanguage();
			testLang.toFile(new File(System.getProperty("user.home") + "/OijonUtils/testish.language"));
			Parser parser2 = new Parser(new File(System.getProperty("user.home") + "/OijonUtils/testish.language"));
			Language compareLanguage = parser2.parseLanguage();
			log.info("Please check the following output for differences.");
			log.info("lastEdited and susquehannaVersion are expected to be different.");
			log.debug(testLang.toString());
			log.info("Language 2:");
			log.debug(compareLanguage.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("=====END LANGUAGE TEST=====");
	}
	
}
