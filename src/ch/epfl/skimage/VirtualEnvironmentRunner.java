package ch.epfl.skimage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ch.epfl.skimage.tools.Log;
import ch.epfl.skimage.tools.OS;

public class VirtualEnvironmentRunner {
	
    private String environmentNameOrPath;

    public VirtualEnvironmentRunner(String environmentNameOrPath) {
        this.environmentNameOrPath = environmentNameOrPath;
    }

    public ArrayList<String> runCommand(ArrayList<String> arguments) throws IOException, InterruptedException {

    	List<String> command = new ArrayList<String>();   	
        if (OS.isWindows()) 
        	command.add(new File(environmentNameOrPath, "Scripts/python").getAbsolutePath());
    	else
    		command.add(new File(environmentNameOrPath, "bin/python").getAbsolutePath());
        command.addAll(arguments);

        List<String> shell = new ArrayList<>();
        if (!OS.isWindows()) {
        	shell.addAll(Arrays.asList("bash", "-c"));
        	// If there are spaces, then we should encapsulate the command with quotes
        	command = command.stream().map(s -> {
                            if (s.trim().contains(" "))
                                return "\"" + s.trim() + "\"";
                            return s;
                        }).collect(Collectors.toList());

        	// The last part needs to be sent as a single string, otherwise it does not run
        	String cmdString = command.toString().replace(",","");
        	shell.add(cmdString.substring(1, cmdString.length()-1));
        }
        else {
        	shell.addAll(Arrays.asList("cmd.exe", "/C"));
        	shell.addAll(command);
        }
        
        // Try to make a command that is fully readable and that can be copy pasted
        List<String> printable = shell.stream().map(s -> {
            // add quotes if there are spaces in the paths
            if (s.contains(" "))
                return "\"" + s + "\"";
            else
                return s;
        }).collect(Collectors.toList());
        String executionString = printable.toString().replace(",", "");
        ArrayList<String> logResults = new ArrayList<>();
        
        Log.section("Executing command");
        Log.write(executionString);
        ProcessBuilder pb = new ProcessBuilder(shell).redirectErrorStream(true);
        Process p = pb.start();

        Thread t = new Thread(Thread.currentThread().getName() + "-" + p.hashCode()) {
            @Override
            public void run() {
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(p.getInputStream()));
                try {
                    for (String line = stdIn.readLine(); line != null; ) {
                        logResults.add(line);
                        line = stdIn.readLine();
                    }
                } 
                catch (IOException e) {
                	logResults.add("Warning " + e.getMessage());
                }
            }
        };
        t.setDaemon(true);
        t.start();
        p.waitFor();
        System.out.println("Virtual Environment Runner: exitValue " + p.exitValue());
        return logResults;
    }
    
}