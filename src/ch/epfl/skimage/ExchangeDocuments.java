package ch.epfl.skimage;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ch.epfl.skimage.tools.OS;
import ij.IJ;
import ij.ImagePlus;

public class ExchangeDocuments {

	private static File dir;
	
	public static void setExchangeDirectory(String dirpath) {
		dir = new File(dirpath);
	}
	
	public static String getExchangeDirectory() {
		return dir.getAbsolutePath() + File.separator;
	}
	
	public static String path(String filename) {
		return dir.getAbsolutePath() + File.separator + filename;
	}
	
	public static String getTemporaryDirectory(String name) {
		if (OS.isMac()) {
			String path = "/tmp";
		 	File tmp = new File(path);
		 	if (tmp.exists())
		 	if (tmp.canWrite()) {
		 		File sub = new File(tmp.getAbsolutePath(), name);
		 		sub.mkdir();
				return sub.getAbsolutePath();
			}
		}
		
		String path = System.getProperty("java.io.tmpdir");
		File tmp = new File(path);
		if (tmp.exists())
		if (tmp.canWrite()) {
	 		File sub = new File(tmp.getAbsolutePath(), name);
	 		sub.mkdir();
			return sub.getAbsolutePath();
		}
		
		path = System.getProperty("user.home");
		File tmp1 = new File(path);
		if (tmp1.exists())
		if (tmp1.canWrite()) {
	 		File sub = new File(tmp.getAbsolutePath(), name);
	 		sub.mkdir();
			return sub.getAbsolutePath();
		}
		
		return System.getProperty("user.home");
	}
	
	
	public String getPermission() {
		String permission = "";
		permission += dir.canRead() ? "read " : "no read ";
		permission += dir.canWrite() ? "write " : "no write ";
		permission += dir.canExecute() ? "execute " : "no execute ";
		
		//permission += "" + (dir.getFreeSpace() / 1024L) + "KB";
		
		return permission;
	}
	
	public static boolean sendImagePlus(ImagePlus imp, String filename) {
		if (imp == null)
			return false;
		if (!dir.exists())
			return false;
		if (!dir.canWrite())
			return false;
		String title = imp.getTitle();
		File file = new File(dir, filename);
		IJ.saveAsTiff(imp, file.getAbsolutePath());
		imp.setTitle(title);
		return true;
	}
	
	public static ImagePlus getImagePlus(String filename) {
		ImagePlus out = IJ.createImage("not found", 10, 10, 1, 8);
		File file = new File(dir, filename);
		if (!file.exists())
			return out;
        out = IJ.openImage(file.getAbsolutePath());
        return out;
	}

	public static boolean sendListPoints(ArrayList<Point2D.Double> list, String filename) {
		if (list == null)
			return false;
		if (!dir.exists())
			return false;
		if (!dir.canWrite())
			return false;
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(ExchangeDocuments.path("temp_init_contour.csv"), true));
			for(Point2D.Double p : list)
				writer.append("" + p.x + ", " + p.y + "\n");
			writer.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

	public static ArrayList<Point2D.Double> getListPoints(String filename) {
		ArrayList<Point2D.Double> list = new ArrayList<Point2D.Double>();
		File file = new File(dir, filename);
		if (!file.exists())
			return list;
		
		BufferedReader reader;		
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				String[] tokens = line.split(",");
				if (tokens.length == 2) {
					Double x = Double.parseDouble(tokens[0]);
					Double y = Double.parseDouble(tokens[1]);
					list.add(new Point2D.Double(x,y));
				}
				line = reader.readLine();
			}
			reader.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return list;

	}
	
	public static ArrayList<String> cleanExchangeDirectory() {
		ArrayList<String> log = new ArrayList<String>();
	    //File[] filesBefore = dir.listFiles();
	    //log.add("Directory " + dir.toString() + " " + filesBefore.length + " files");
		eraseFiles(dir, log);
		File[] filesAfter = dir.listFiles();
		log.add("Directory " + dir.toString() + ": " + filesAfter.length + " files");
		return log;
	}
	
    private static void eraseFiles(File directory, ArrayList<String> log) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (file.delete()) {
                        System.out.println("Deleted file: " + file.getAbsolutePath());
                        //log.add("Delete file: " + file.getAbsolutePath());
                    } 
                    else {
                        System.err.println("Failed to delete file: " + file.getAbsolutePath());
                        //log.add("Failed to delete file: " + file.getAbsolutePath());
                    }
                } 
                else if (file.isDirectory()) {
                    eraseFiles(file, log); // Recursively delete files in subdirectories
                }
            }
        }
    }
	
}
