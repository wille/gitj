package com.redpois0n.gitj.utils;

import java.io.File;

import com.redpois0n.git.Repository;

public class FileUtils {
	
	public static String getRepoPath(File file, Repository repo) {
		String path = file.getAbsolutePath();
		
		path.replace(repo.getFolder().getAbsolutePath(), "").replace("\\", "/");
		
		if (path.startsWith("/")) {
			path = path.substring(1, path.length());
		}
		
		return path;
	}
	
	public static File fromRepoPath(String path, Repository repo) {
		return new File(repo.getFolder(), path);
	}

}
