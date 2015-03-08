package com.redpois0n.gitj.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Repository {
	
	private File folder;
	private List<Commit> commits;
	
	public Repository(File folder) {
		this.folder = folder;
	}
	
	/**
	 * Returns all commits from this repository
	 * @return Commits from cache
	 * @throws Exception
	 */
	public List<Commit> getCommits() throws Exception {
		return getCommits(false);
	}
	
	/**
	 * Returns all commits from this repository
	 * @param update If git log should be executed and read
	 * @return Commits either freshly loaded or from cache
	 * @throws Exception
	 */
	public List<Commit> getCommits(boolean update) throws Exception {
		if (!update && commits != null) {
			return commits;
		} else {
			if (commits == null) {
				commits = new ArrayList<Commit>();
			} else {
				commits.clear();
			}
			
			List<String> raw = run(new String[] { "git", "log", "--pretty-format:\"%H;%an;%ae;%ar;%s\"" }); 
			
			for (String s : raw) {
				System.out.println(s);
			}
			
			return commits;
		}
	}
	
	public List<String> run(String c) throws Exception {
		return run(c.split(" "));
	}
	
	/**
	 * Executes process with specified arguments
	 * @param c
	 * @return ArrayList with all lines that has been read, empty list if none
	 * @throws Exception
	 */
	public List<String> run(String[] c) throws Exception {
		ProcessBuilder pb = new ProcessBuilder(c);
		pb.directory(folder);
		Process p = pb.start();

		List<String> lines = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		
		reader.close();
	
		return lines;
	}

}
