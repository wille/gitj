package com.redpois0n.gitj.utils;

import javax.swing.JOptionPane;

public class DialogUtils {
	
	public static boolean confirm(String message, String title) {
		return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
	}

}
