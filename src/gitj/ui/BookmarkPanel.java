package gitj.ui;

import iconlib.IconUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.redpois0n.git.Repository;
import gitj.utils.StatusUtils;

@SuppressWarnings("serial")
public class BookmarkPanel extends JPanel {
	
	private BookmarksPanel parent;
	private Repository repo;
	
	private JToolBar b1;
	private JToolBar b2;
	
	public BookmarkPanel(BookmarksPanel parent, Repository repo) {
		this.parent = parent;
		this.repo = repo;
				
		setLayout(new BorderLayout(0, 0));
		
		reload();
	}
	
	public Repository getRepository() {
		return this.repo;
	}

	public void reload() {
		removeAll();
		
		b1 = new JToolBar();
		b1.setFloatable(false);
		add(b1, BorderLayout.NORTH);

		b2 = new JToolBar();
		b2.setFloatable(false);
		add(b2, BorderLayout.SOUTH);
		
		JLabel lblName = new JLabel(repo.getName() + "  ");
		lblName.setIcon(IconUtils.getIcon("icon"));
		b1.add(lblName);
		
		JLabel lblPath = new JLabel(repo.getFolder().getAbsolutePath());
		lblPath.setForeground(Color.gray);
		b1.add(lblPath);
		
		for (JLabel label : StatusUtils.getIcons(repo)) {
			b2.add(label);
		}
		
		b2.add(Box.createHorizontalGlue());
		
		JButton btnRemove = new JButton();
		btnRemove.setIcon(IconUtils.getIcon("cross"));
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.removeBookmarkPanel(BookmarkPanel.this);
			}
		});
		b2.add(btnRemove);
		
		JButton btnReload = new JButton();
		btnReload.setIcon(IconUtils.getIcon("blue-folder-open"));
		btnReload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.getParentFrame().loadRepository(repo);
			}
		});
		b2.add(btnReload);
		
		revalidate();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof BookmarkPanel) {
			BookmarkPanel panel = (BookmarkPanel) o;
			
			return panel.repo.equals(this.repo);
		}
		
		return this == o;
	}
}
