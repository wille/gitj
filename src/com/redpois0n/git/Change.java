package com.redpois0n.git;

import java.util.ArrayList;
import java.util.List;

public class Change {
	
	public static enum Type {
		ADDED("A*", true), 
		STAGED_DELETE("D*", true), 
		UNSTAGED_DELETE("*D", false),
		STAGED_RENAME("R*", true), 
		UNSTAGED_RENAME("*R", false),
		STAGED_COPY("C*", true), 
		UNSTAGED_COPY("*C", false),
		STAGED_MODIFIED("M*", true),
		UNSTAGED_MODIFIED("*M", false),
		UNSTAGED("??", false);
		
		private String x;
		private boolean staged;
		
		private Type(String x, boolean staged) {
			this.x = x;
			this.staged = staged;
		}
		
		public String getX() {
			return this.x;
		}
		
		public boolean isStaged() {
			return this.staged;
		}
	}
	
	private List<Type> types;
	private String repoPath;
	
	public Change(List<Type> types, String repoPath) {
		this.types = types;
		this.repoPath = repoPath;
	}
	
	public String getRepoPath() {
		return repoPath;
	}
	
	public List<Type> getTypes() {
		return types;
	}
	
	public Type getFirstType() {
		return types.get(0);
	}
	
	public boolean isStaged() {		
		boolean b1 = types.get(0).isStaged();
		
		for (int i = 0; i < types.size(); i++) {
			boolean tstag = types.get(i).isStaged();
			
			assert b1 == tstag;
		}
		
		return b1;
	}
	
	public static List<Type> getType(String s) {
		List<Type> types = new ArrayList<Type>();;
		
		for (Type t : Type.values()) {
			String x = t.getX();
			
			if (x.startsWith("*") && s.endsWith(x.replace("*", "")) || x.endsWith("*") && s.startsWith(x.replace("*", "")) || x.equals(s)) {
				types.add(t);
			}
		}
		
		return types;
	}

}
