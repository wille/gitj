package gitj.ui;

import javax.swing.JPanel;

import git.Repository;

@SuppressWarnings("serial")
public abstract class AbstractPanel extends JPanel {
	
	protected AbstractPanel parent;
	protected Repository repo;
	
	public AbstractPanel(Repository repo) {
		this.repo = repo;
	}
	
	public AbstractPanel(AbstractPanel parent) {
		this.parent = parent;
	}
	
	public Repository getRepository() {
		return this.parent == null ? this.repo : this.parent.getRepository();
	}
	
	public AbstractPanel getParentPanel() {
		return this.parent == null ? this : this.parent;
	}
	
	public void reload() throws Exception {
		
	}

}
