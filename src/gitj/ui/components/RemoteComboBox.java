package gitj.ui.components;

import java.awt.Component;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

import com.redpois0n.git.Remote;
import com.redpois0n.git.Repository;

@SuppressWarnings("serial")
public class RemoteComboBox extends JComboBox<Remote> {

	private DefaultComboBoxModel<Remote> model;

	public RemoteComboBox(Repository repo) throws Exception {
		model = new DefaultComboBoxModel<Remote>();
		
		super.setRenderer(new Renderer());
		super.setModel(model);

		reload(repo.getRemotes());
	}
	
	public Remote getSelectedRemote() {
		return (Remote) model.getSelectedItem();
	}
	
	public void reload(List<Remote> list) {
		model.removeAllElements();

		for (Remote remote : list) {
			this.addItem(remote);
		}
	}

	public class Renderer extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			if (value instanceof Remote) {
				Remote remote = (Remote) value;
				
				label.setText(remote.getName() + ", " + remote.getPath());
			}
						
			return label;
		}
	}
}
