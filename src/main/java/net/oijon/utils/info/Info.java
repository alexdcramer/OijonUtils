package net.oijon.utils.info;

import java.io.IOException;
import java.util.Properties;

//last edit: 8/14/23 -N3

/**
 * A class to get the version information of the current build
 * @author alex
 *
 */
public class Info {

	/**
	 * Gets the current version of Oijon Utils, for example "Oijon Utils - v1.2.0"
	 * @return The current version of Oijon Utils
	 */
	public static String getVersion() {
		return "Oijon Utils - v" + getVersionNum();
	}
	
	/**
	 * Gets the version number of Oijon Utils, for example "1.1.1"
	 * @return The current version number of Oijon Utils
	 */
	public static String getVersionNum() {
		Properties properties = new Properties();
		String returnString = "UNKNOWN";
		try {
			properties.load(Info.class.getResourceAsStream("project.properties"));
			returnString = properties.getProperty("version");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnString;
	}
	
}
