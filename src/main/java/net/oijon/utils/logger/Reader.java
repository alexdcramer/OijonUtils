package net.oijon.utils.logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.oijon.utils.info.Info;

public class Reader {

	/**
	 * Prints an Oijon Utils log file to console
	 */
	public static void readLog(String fileName, String destinationFile) {
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
	        List<String> lines = new ArrayList<String>();
	        String line = null;
	        while ((line = bufferedReader.readLine()) != null) {
	            lines.add(line);
	        }
	        bufferedReader.close();
	        String[] lineArray = lines.toArray(new String[lines.size()]);
	        
	        Log log = new Log(destinationFile, false);
	        
	        if (lineArray[4].equals("===BEGIN OIJON LOG===") == false) {
	        	log.err("This file was not made with Oijon Utils! Oijon Utils will attempt to read this file, however color may not be supported.");
	        } else if (lineArray[2].equals("versionNum: " + Info.getVersionNum()) == false) {
	        	log.warn("This log was made with a different version of Oijon Utils! Features not shared between these two versions may not be supported.");
	        	log.warn("Current version: " + Info.getVersionNum());
	        	String[] foundVersion = lineArray[2].split(": ");
	        	log.warn("Log version: " + foundVersion[1]);
	        }
	        log.info("===BEGIN LOG READING FROM " + fileName + "===");
	        int noColor = 0;
	        for (int i = 5; i < lineArray.length; i++) {
	        	String lineType = lineArray[i].substring(0, 11);
	        	if (lineType.equals("[DEBUG]    ")) {
	        		log.debug(lineArray[i]);
	        	}
	        	else if (lineType.equals("[INFO]     ")) {
	        		log.info(lineArray[i]);
	        	}
	        	else if (lineType.equals("[WARN]     ")) {
	        		log.warn(lineArray[i]);
	        	}
	        	else if (lineType.equals("[ERROR]    ")) {
	        		log.err(lineArray[i]);
	        	}
	        	else if (lineType.equals("[CRITICAL] ")) {
	        		log.critical(lineArray[i]);
	        	}
	        	else {
	        		noColor++;
	        		log.info(lineArray[i]);
	        	}
	        }
	        if (noColor != 0) {
	        	log.warn(noColor + " lines could not be colorized.");
	        }
	        log.info("====END LOG READING FROM " + fileName + "====");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
