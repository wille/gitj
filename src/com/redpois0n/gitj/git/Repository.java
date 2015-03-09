package com.redpois0n.gitj.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
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
			
			List<String> raw = run(new String[] { "git", "log", "--pretty=format:\"Commit;%H;%an;%ae;%ar;%s\"", "--stat", "-p" }); 
			Enumeration<String> e = Collections.enumeration(raw);
			
			String s = e.nextElement();
			
			while (e.hasMoreElements()) {
				List<Diff> diffs = new ArrayList<Diff>();
				
				System.out.println("Raw commit data: " + s);
				Commit c = new Commit(this, s);

				while ((s = e.nextElement()).contains("|")) {
					String sdiff = s.split("|")[0].trim();
					Diff diff = new Diff();
					System.out.println("Diff created: " + s);
				}
								
				String changes = s;
				System.out.println("Changes: " + changes);
				
				s = e.nextElement();
								
				while (s.startsWith("diff --git")) {
					System.out.println("Diff: " + s);

					
					s = e.nextElement();
					
					while (!(s = e.nextElement()).startsWith("diff --git")) {
						String codeline = s;
						
						if (e.hasMoreElements()) {
							//s = e.nextElement();
						}

						if (s.startsWith("Commit;") || !e.hasMoreElements()) {
							break;
						} else if (s.startsWith("@@ ")) {
							System.out.println("Chunk: " + s);
						} else {
							System.out.println("Code: " + s);
							//code.add(s);
						}
					}
					
				}
				
				commits.add(c);
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
			if (line.length() > 0) {
				lines.add(line);
			}
		}
		
		reader.close();
			
		return lines;
	}

}
