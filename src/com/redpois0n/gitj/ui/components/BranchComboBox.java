package com.redpois0n.gitj.ui.components;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

import com.redpois0n.git.Branch;
import com.redpois0n.git.Repository;
import com.redpois0n.gitj.Main;

@SuppressWarnings("serial")
public class BranchComboBox extends JComboBox<Branch> {

	private Repository repository;
	private DefaultComboBoxModel<Branch> model;

	public BranchComboBox(Repository repo) throws Exception {
		this.repository = repo;
		model = new DefaultComboBoxModel<Branch>();
		
		super.setRenderer(new Renderer());
		super.setModel(model);

		super.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					Object o = event.getItem();

					if (o instanceof Branch) {
						Branch branch = (Branch) o;
												
						try {
							repository.checkout(branch);
						} catch (Exception e1) {
							e1.printStackTrace();
							Main.displayError(e1);
						}
					}
				}
			}
		});

		reload(repo.getBranches());
	}
	
	public void reload(List<Branch> list) {
		model.removeAllElements();

		for (Branch branch : list) {
			this.addItem(branch);
		}
	}

	public class Renderer extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			if (value instanceof Branch) {
				Branch branch = (Branch) value;
				
				label.setText(branch.getName());
				
				if (branch.isSelected()) {
					label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
				}
			}
						
			return label;
		}
	}
}
