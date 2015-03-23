package com.redpois0n.gitj.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.redpois0n.git.Repository;

public class FileUtils {
	
	public static String getRepoPath(File file, Repository repo) {
		String path = file.getAbsolutePath();
		
		path = path.replace(repo.getFolder().getAbsolutePath(), "").replace("\\", "/");
		
		if (path.startsWith("/")) {
			path = path.substring(1, path.length());
		}
		
		return path;
	}
	
	public static File fromRepoPath(String path, Repository repo) {
		return new File(repo.getFolder(), path);
	}
	
	public static File writeToTempFile(String contents) throws Exception {
		File file = File.createTempFile("tmp", ".txt");
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		writer.write(contents);
		writer.close();
		
		return file;
	}
	
	public static List<String> readFile(File file) throws Exception {
		List<String> lines = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		String line;
		
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		
		reader.close();
		
		return lines;
	}

}
