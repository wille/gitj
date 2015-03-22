package com.redpois0n.git;

public class Branch {

	private String name;
	private Commit commit;
	private String status;

	public Branch(String name, Commit commit) {
		this.name = name;
		this.commit = commit;
	}

	public String getName() {
		return name;
	}

	public Commit getCommit() {
		return commit;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
