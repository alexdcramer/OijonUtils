package net.oijon.utils.info;

//last edit: 5/23/2023 -N3

/**
 * A class to get the version information of the current build
 * @author alex
 *
 */
public class Info {

	private static String versionNum = "1.2.2";
	private static String fullVersion = "Oijon Utils - v" + versionNum;
	
	/**
	 * Gets the current version of Oijon Utils, for example "Oijon Utils - v1.2.0"
	 * @return The current version of Oijon Utils
	 */
	public static String getVersion() {
		return fullVersion;
	}
	
	/**
	 * Gets the version number of Oijon Utils, for example "1.1.1"
	 * @return The current version number of Oijon Utils
	 */
	public static String getVersionNum() {
		return versionNum;
	}
	
}
