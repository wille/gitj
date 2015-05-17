package com.redpois0n.gitj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Bookmarks {
	
	public static final File FILE_BOOKMARKS = new File(".bookmarks");
	
	public static List<String> load() {
		List<String> lines = new ArrayList<String>();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_BOOKMARKS)));

			String line;
			
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			
			reader.close();
		} catch (FileNotFoundException e) {

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lines;
	}
	
	public static void save(List<String> bookmarks) throws Exception {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_BOOKMARKS)));
		
		for (String s : bookmarks) {
			writer.write(s);
			writer.newLine();
		}
		
		writer.close();
	}

}
