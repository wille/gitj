package com.redpois0n.gitj;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redpois0n.git.Repository;
import com.redpois0n.gitj.utils.FileUtils;

public class LanguageScanner {
	
	private final Map<File, Integer> count = new HashMap<File, Integer>();
	private Repository repo;

	public LanguageScanner(Repository repo) {
		this.repo = repo;
	}

	public List<Language> scan(boolean all) throws Exception {
		List<String> raw = FileUtils.readFile(new File("languages.yml"));

		List<Language> langs = new ArrayList<Language>();
		
		List<String> extensions = null;
		Language temp = null;
		
		for (String line : raw) {
			 if (line.startsWith("  type: programming")) {
	            if (!all) {
	            	langs.add(temp);
	            }
			 } else if (line.startsWith("  color: \"#")) {
		         String c = line.replace("  color: \"#", "").replace("\"", "").trim();
		         temp.setColor(Color.decode("0x" + c));
			 } else if (line.startsWith("  - .")) {
		         String extension = line.replace("  - .", "").trim();
		         extensions.add(extension);
			 } else if (line.startsWith(" ")) {
				 continue;
			 } else {
		         line = line.replace(":", "").trim();
		         if (line.length() > 0) {
		             temp = new Language(line);
		             extensions = new ArrayList<String>();
		             temp.setExtensions(extensions);
		             if (all) {
		            	 langs.add(temp);
		             }
		         }
			}
		}
		
		check(repo.getTrackedFiles(), langs, repo.getFolder());
		
		List<Language> used = new ArrayList<Language>();
		
		for (Language l : langs) {
			if (l.getLineCount() > 0 && l.getFiles() > 0) {
				used.add(l);
			}
		}
		
		Collections.sort(used);
		
		return used;

	}
	
	private void check(List<File> tracked, List<Language> langs, File dir) throws Exception {
		for (File file : dir.listFiles()) {			
			if (file.isDirectory()) {
				check(tracked, langs, file);
			} else {
				for (Language lang : langs) {
					for (String s : lang.getExtensions()) {
						if (file.getName().endsWith("." + s) && tracked.contains(file)) {
							lang.incrementFiles();
							lang.addLineCount(count(file));
						}
					}
				}
			}
		}		
	}
	
	private int count(File file) throws Exception {
		if (count.containsKey(file)) {
			return count.get(file);
		} else {
			int c = FileUtils.countLines(file);
			
			count.put(file, c);
			
			return c;
		}
	}
}
