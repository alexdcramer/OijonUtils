package net.oijon.utils.logger;

import java.io.BufferedWriter;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Properties;
//import java.util.Scanner;
import com.diogonunes.jcolor.AnsiFormat;
import static com.diogonunes.jcolor.Attribute.*;

//last edit: 8/14/2023 -N3



/**
 * Simple log utility to help with getting console output to file
 * @author N3ther
 *
 */
public class Log {
	
	private boolean debug = true;
	private File file;
	private String today;
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private String now;
	private File tempFile;
	
	AnsiFormat fDebug = new AnsiFormat(WHITE_TEXT());
	AnsiFormat fInfo = new AnsiFormat(CYAN_TEXT());
	AnsiFormat fWarn = new AnsiFormat(BLACK_TEXT(), YELLOW_BACK());
	AnsiFormat fError = new AnsiFormat(WHITE_TEXT(), RED_BACK());
	AnsiFormat fCritical = new AnsiFormat(BOLD(), RED_TEXT(), YELLOW_BACK());
	
	/**
	 * Creates the log object. This should only be used in the main class, unless you want multiple log files.
	 * Please note: This will create a directory under the directory specified called "/logs/".
	 * @param logdir The directory, in string format, of the log.
	 */
	public Log(String logdir) {
		this(logdir, false);
	}
	
	/**
	 * Creates the log object.
	 * Please note: This will create a directory under the directory specified called "/logs/".
	 * @param logdir The directory, in string format, of the log.
	 * @param useCurrent True if a log file already exists, false otherwise
	 */
	public Log(String logdir, boolean useCurrent) {
		File logFolder = new File(logdir + "/logs/");
		logFolder.mkdirs();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.today = LocalDate.now().format(formatter);
		File logFile = new File(logdir + "/logs/" + this.today + ".log");
		try {
			logFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File tempFile = new File(logdir + "/logs/.logtmp");
		int i = 0;
		while (logFile.exists()) {
			
			i++;
			logFile = new File(logdir + "/logs/" + this.today + "(" + i + ")" + ".log");
		}
		if (useCurrent) {
			if (i > 0) {
				i--;
				if (i == 0) {
					logFile = new File(logdir + "/logs/" + this.today + ".log");
				} else {
					logFile = new File(logdir + "/logs/" + this.today + "(" + i + ")" + ".log");
				}
			}
		} else {
			try {
				File logDir = new File(logdir + "/logs/");
				logDir.mkdirs();
				logFile.createNewFile();
				tempFile.createNewFile();
				FileWriter fw = new FileWriter(tempFile, true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(logFile.toString());
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.file = logFile;
	}
	
	/**
	 * Sets the definition of "now", as in what is the time the print was sent out
	 */
	private void setNow() {
		LocalDateTime now = LocalDateTime.now();
		this.now = this.dtf.format(now);
	}
	
	/**
	 * Reads the temp file and gets the current log file
	 * @return
	 
	private File readTempFile() {
		try {
			Scanner sc = new Scanner(tempFile);
			String filePath = "";
			while(sc.hasNextLine()) {
				filePath = sc.nextLine();
			}
			sc.close();
			File logFile = new File(filePath);
			return logFile;
		} catch (FileNotFoundException e) {
			this.err(e.toString());
			return null;
		}
	}
	*/
	
	/**
	 * Logs console input to the log
	 * @param input The raw input
	 */
	public void input(String input) {
		write(">" + input);
	}
	
	/**
	 * Prints a debug line
	 * @param input What is to be printed
	 */
	public void debug(String input) {
		if (debug) {
			log("DEBUG", input, fDebug);
		}
	}
	
	/**
	 * Prints an info line
	 * @param input What is to be printed
	 */
	public void info(String input) {
		log("INFO", input, fInfo);
	}
	
	/**
	 * Prints a warning
	 * @param input What is to be printed
	 */
	public void warn(String input) {
		log("WARN", input, fWarn);
	}
	
	/**
	 * Prints an error.
	 * @param input What is to be printed
	 */
	public void err(String input) {
		log("ERROR", input, fError);
	    
	}
	/**
	 * Prints a critical error.
	 * @param input What is to be printed
	 */
	public void critical(String input) {
		log("CRITICAL", input, fCritical);
	}
	
	/**
	 * Prints system information to console and writes to file
	 */
	public void logSystemInfo() {
		Properties properties = System.getProperties();
		this.debug("=====================");
		this.debug("List of system properties:");
		Enumeration<Object> keyNames = properties.keys();
		while(keyNames.hasMoreElements()) {
			String key = keyNames.nextElement().toString();
			String value = properties.getProperty(key).toString();
			this.debug(key + " - " + value);
		}
		this.debug("=====================");
	}
	
	public void setDebug(boolean bool) {
		debug = bool;
	}
	
	/**
	 * Prints out to a file and to console given a prefix and color
	 * @param prefix The prefix of the line to print, for example [INFO]
	 * @param input The message to print
	 * @param color The color to print in the console
	 */
	private void log(String prefix, String input, AnsiFormat color) {
		setNow();
		String output = String.format("%-10s", "[" + prefix + "]") + 
				" [" + this.now + "] - " + input;
		System.out.println(color.format(output));
		write(output);
	}
	
	/**
	 * Writes a string to the log file
	 * @param input The string to be written into the log file
	 */
	private void write(String input) {
		try {
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(input);
			bw.newLine();
		    bw.close();
		} catch (IOException e) {
			// if this catch is being hit, something has gone horribly wrong
			this.err(e.toString());
			e.printStackTrace();
		}
	}
	/**
	 * Currently does not do anything, as the temp file does not actually control the file used anymore
	 * @deprecated As of Oijon Utils v1.1.2, as the way file selection is handled has changed. Please use the true/false flag on log creation instead.
	 */
	@Deprecated
	public void closeLog() {
		this.info("Closing log...");
		this.tempFile.delete();
		this.info("Log closed.");
	}
	
	/**
	 * Gets the current file a log is writing to.
	 * @return The path to the current file
	 */
	public String getLogFile() {
		return file.toString();
	}
}
