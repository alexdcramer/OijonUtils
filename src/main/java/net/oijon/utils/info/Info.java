package net.oijon.utils.info;

public class Info {

	private static String versionNum = "1.1.0";
	private static String fullVersion = "Oijon Utils - v" + versionNum;
	
	public static String getVersion() {
		return fullVersion;
	}
	public static String getVersionNum() {
		return versionNum;
	}
	
}
