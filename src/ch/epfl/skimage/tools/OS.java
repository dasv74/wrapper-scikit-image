package ch.epfl.skimage.tools;

public class OS {

	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().startsWith("windows");
	}

	public static boolean isMac() {
		return System.getProperty("os.name").toLowerCase().startsWith("mac os x");		
	}
	
	public static boolean isLinux() {
		return System.getProperty("os.name").toLowerCase().startsWith("linux");		
	}
}
