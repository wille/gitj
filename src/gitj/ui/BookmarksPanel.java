package gitj.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.redpois0n.git.Repository;

@SuppressWarnings("serial")
public class BookmarksPanel extends JPanel {
	
	private List<BookmarkPanel> panels = new ArrayList<BookmarkPanel>();
	
	private MainFrame mainFrame;
		
	public BookmarksPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		super.setBorder(new EmptyBorder(0, 0, 0, 0));
		
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
		setBackground(Color.white);
	}
	
	public MainFrame getParentFrame() {
		return mainFrame;
	}
	
	public void addBookmarkPanel(BookmarkPanel panel) {
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		add(panel, BorderLayout.NORTH);
		
		if (!panels.contains(panel)) {
			panels.add(panel);
		}
	}
	
	public void removeBookmarkPanel(BookmarkPanel panel) {
		panels.remove(panel);
		remove(panel);
		repaint();
	}
	
	public BookmarkPanel getFromRepo(Repository repo) {
		for (BookmarkPanel panel : panels) {
			if (panel.getRepository().equals(repo)) {
				return panel;
			}
		}
		
		return null;
	}
	
	public List<BookmarkPanel> getPanels() {
		return panels;
	}

	/**
	 * Tries to reload bookmark panel if it exists for a repo
	 * @param repo
	 */
	public void reload(Repository repo) {
		BookmarkPanel panel = getFromRepo(repo);
		
		if (panel != null) {
			panel.reload();
		}
	}

	public List<String> getBookmarks() {
		List<String> paths = new ArrayList<String>();
		
		for (BookmarkPanel panel : panels) {
			paths.add(panel.getRepository().getFolder().getAbsolutePath());
		}
		
		return paths;
	}

}
