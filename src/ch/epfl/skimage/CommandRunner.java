package ch.epfl.skimage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ch.epfl.skimage.tools.OS;

public class CommandRunner {

	static public ArrayList<String> run(String command) {
		ArrayList<String> output = new ArrayList<String>();
		try {
			ProcessBuilder processBuilder;
			if (OS.isWindows()) {
				processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
			} 
			else {
				processBuilder = new ProcessBuilder("bash", "-c", "\"command\"");
			}
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				output.add(line);
			}
			int exitCode = process.waitFor();
			System.out.println("Command exited with code: " + exitCode);
		} 
		catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return output;
	}
}
