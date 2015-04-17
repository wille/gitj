package com.redpois0n.git;

public class Branch {

	private String name;
	private Commit commit;
	private String status;
	private boolean isSelected;

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
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Branch) {
			Branch branch = (Branch) o;
			
			return branch.commit != null && branch.name != null && branch.commit.equals(this.commit) && branch.name.equals(this.name);
		}
		
		return o == this;
	}
}
