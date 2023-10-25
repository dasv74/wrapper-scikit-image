package ch.epfl.skimage.tools;

import java.util.ArrayList;

import javax.swing.JScrollPane;

import ch.epfl.skimage.components.HTMLPane;

public class Log {

	static private HTMLPane info;
	
	static {
		info = new HTMLPane(500, 100);
	}
	
	public static JScrollPane getPane() {
		return new JScrollPane(info);
	}
	
	public static void section(String message) {
		info.append("h2", message);
	}
	
	public static void write(String message) {
		info.append("p", message);
	}
	
	public static void write(ArrayList<String> list) {
		for(String item : list)
			info.append("p", item);
	}
	
	
	
}
