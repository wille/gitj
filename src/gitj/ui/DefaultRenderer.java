package gitj.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class DefaultRenderer extends DefaultTableCellRenderer {

	public static final Color TABLE_SELECTED = new Color(51, 153, 255);
	public static final Color TABLE_GRAY = new Color(240, 240, 240);
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (isSelected) {
			label.setBackground(TABLE_SELECTED);
		} else if (row % 2 == 0) {
			label.setBackground(TABLE_GRAY);
		} else {
			label.setBackground(Color.white);
		}

		return label;
	}
}