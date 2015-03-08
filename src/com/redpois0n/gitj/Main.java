package com.redpois0n.gitj;

import java.io.File;

import com.redpois0n.gitj.git.Repository;

public class Main {

	public static void main(String[] args) throws Exception {
		try {
			File dir = new File(".");
			Repository repository = new Repository(dir);
			repository.getCommits();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
