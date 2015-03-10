package com.redpois0n.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import com.redpois0n.gitj.Main;

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
			
			List<String> raw = run(new String[] { "git", "log", "--pretty=format:Commit;%H;%an;%ae;%ar;%s", "--stat", "-p" }); 
			Enumeration<String> e = Collections.enumeration(raw);
			
			String s = e.nextElement();
			
			while (e.hasMoreElements()) {				
				Main.print("Raw commit data: " + s);
				Commit c = new Commit(this, s.replace("Commit;", ""));

				while ((s = e.nextElement()).contains("|")) {
					//String sdiff = s.split("|")[0].trim();
					//Diff diff = new Diff();
					//Main.print("Diff created: " + s);
				}
								
				String changes = s;
				Main.print("Changes: " + changes);
				
				s = e.nextElement();
								
				while (s.startsWith("diff --git")) {
					Main.print("Diff: " + s);

					String sdiff = s.substring(s.lastIndexOf(" b/") + 3, s.length()).trim();		
										
					s = e.nextElement();
					
					Diff.Type type;
					
					if (s.startsWith("deleted")) {
						type = Diff.Type.DELETED;
					} else if (s.startsWith("new")) {
						type = Diff.Type.NEW;
					} else {
						type = Diff.Type.EDITED;
					}
					
					Diff diff = new Diff(c, new File(folder, sdiff), type);
					c.addDiff(diff);
					
					Chunk current = null;
					
					while (!(s = e.nextElement()).startsWith("diff --git")) {
						if (s.startsWith("Commit;") || !e.hasMoreElements()) {
							break;
						} else if (s.startsWith("@@ ")) {
							String chunk = s.substring(0, s.indexOf("@@", 3) + 2).trim();
							
							Main.print("Chunk: " + chunk);				
							
							s = " " + s.substring(s.indexOf("@@", 3) + 2, s.length());

							current = new Chunk(diff, chunk);
							diff.addChunk(current);
						}
						
						if (current != null) {
							current.addRawLine(s);		
							Main.print("Code: " + s);
						} else {
							Main.print("!Code: " + s);
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
	
	public boolean hasUnstagedFiles() throws Exception {
		List<String> raw = getStatus(); 
				
		return raw.size() > 0;
	}
	
	public List<String> getStatus() throws Exception {
		return run(new String[] { "git", "status", "--short", "--porcelain" });
	}

	public List<Change> parseStatus() throws Exception {
		List<String> raw = getStatus();
		List<Change> changes = new ArrayList<Change>();
		
		for (String s : raw) {
			String xy = s.substring(0, 2);
			String path = s.substring(3, s.length());
			changes.add(new Change(Change.getType(xy), path));
		}
		
		return changes;
	}
	
	public void stage(String path) throws Exception {
		List<String> raw = run(new String[] { "git", "add", path });

		for (String s : raw) {
			Main.print(s);
		}
	}
	
	
	public void unstage(String path) throws Exception  {
		List<String> raw = run(new String[] { "git", "reset", path });
		
		for (String s : raw) {
			Main.print(s);
		}
	}
	
	public void init() throws Exception {
		List<String> raw = run(new String[] { "git", "init" }); 

		for (String s : raw) {
			Main.print(s);
		}
	}
	
	/**
	 * Gets file content at commit c
	 * @param c
	 * @param repopath
	 * @return
	 * @throws Exception
	 */
	public String getFileAt(Commit c, String repopath) throws Exception {
		List<String> raw = run(new String[] { "git", "show", c.getHash() + ":" + repopath }); 
		
		if (raw.get(0).startsWith("fatal: Path ") || raw.get(0).startsWith("fatal: Invalid object name")) {
			throw new FileNotFoundException(raw.get(0));
		}
		
		StringBuilder sb = new StringBuilder();

		for (String s : raw) {
			sb.append(s);
		}
		
		return sb.toString();
	}
	
	public File getFolder() {
		return this.folder;
	}
	
	public String getName() {
		return getFolder().getName();
	}

}
