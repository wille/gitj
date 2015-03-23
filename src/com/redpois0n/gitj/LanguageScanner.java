package com.redpois0n.gitj;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.redpois0n.git.Repository;
import com.redpois0n.gitj.ui.components.Language;
import com.redpois0n.gitj.utils.FileUtils;

public class LanguageScanner {
	
	private Repository repo;

	public LanguageScanner(Repository repo) {
		this.repo = repo;
	}

	public List<Language> scan() throws Exception {
		List<String> raw = FileUtils.readFile(new File("languages.yml"));

		List<Language> langs = new ArrayList<Language>();
		
		List<String> extensions = null;
		Language temp = null;
		
		for (String line : raw) {
			 if (line.startsWith("  color: \"#")) {
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
		             langs.add(temp);
		             extensions = new ArrayList<String>();
		             temp.setExtensions(extensions);
		         }
			}
		}
		
		check(langs, repo.getFolder());
		
		List<Language> used = new ArrayList<Language>();
		
		for (Language l : langs) {
			if (l.getLineCount() > 0 && l.getFiles() > 0) {
				used.add(l);
			}
		}
		
		return used;

	}
	
	private void check(List<Language> langs, File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				check(langs, file);
			} else {
				for (Language lang : langs) {
					for (String s : lang.getExtensions()) {
						if (file.getName().endsWith("." + s)) {
							lang.incrementFiles();
							lang.addLineCount(10);
						}
					}
				}
			}
		}		
	}
}
