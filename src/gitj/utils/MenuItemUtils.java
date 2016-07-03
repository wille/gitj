package gitj.utils;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.redpois0n.git.Commit;
import gitj.Main;

public class MenuItemUtils {
	
	public static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	public static JMenuItem getOpenSelectedVersion(final Commit c, final String repopath) {
		JMenuItem item = new JMenuItem();
		item.setText("Open selected version");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					String contents = c.getRepository().getFileAt(c, repopath);
					File file = FileUtils.writeToTempFile(contents);
					Desktop.getDesktop().open(file);
				} catch (Exception ex) {
					ex.printStackTrace();
					Main.displayError(ex);
				}
			}
		});
		
		return item;
	}

}
