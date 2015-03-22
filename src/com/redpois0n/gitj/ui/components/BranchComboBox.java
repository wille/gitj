package com.redpois0n.gitj.ui.components;

import java.awt.Component;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

import com.redpois0n.git.Branch;
import com.redpois0n.git.Repository;

@SuppressWarnings("serial")
public class BranchComboBox extends JComboBox<Branch> {

	private DefaultComboBoxModel<Branch> model;
	
	public BranchComboBox(Repository repo) throws Exception {
		this(repo.getBranches());
	}

	public BranchComboBox(List<Branch> list) {
		model = new DefaultComboBoxModel<Branch>();
		
		super.setRenderer(new Renderer());
		super.setModel(model);
		
		reload(list);
	}
	
	public void reload(List<Branch> list) {
		model.removeAllElements();
		
		for (Branch branch : list) {
			model.addElement(branch);
		}
	}

	public class Renderer extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			if (value instanceof Branch) {
				Branch branch = (Branch) value;
				
				label.setText(branch.getName());
			}
			
			return label;
		}
	}
}
