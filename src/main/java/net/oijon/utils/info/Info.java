package net.oijon.utils.info;

import java.io.FileReader;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

//last edit: 8/14/23 -N3

/**
 * A class to get the version information of the current build
 * @author alex
 *
 */
public class Info {

	private static String versionNum = generateVersionNum();
	private static String fullVersion = "Oijon Utils - v" + versionNum;
	
	private static String generateVersionNum() {
		try {
			MavenXpp3Reader reader = new MavenXpp3Reader();
	        Model model = reader.read(new FileReader("pom.xml"));
	        return model.getVersion();
		} catch (Exception e) {
			e.printStackTrace();
			return "null";
		}
	}
	
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
