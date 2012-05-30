package org.alder.fotobuchconvert.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

public class MainForm extends JFrame {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		MainForm frame = new MainForm();
		frame.setVisible(true);
	}

	public MainForm() {
		init();
		pack();
	}

	private void init() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationByPlatform(true);

		setLayout(new GridBagLayout());

		add(new JLabel("Source type"), gc(0, 0));
		add(new JList(), gc(0, 1));
		add(new JLabel("Source file"), gc(0, 2));
		add(new JTextField(), gc(0, 3));
		add(new JLabel("Destination type"), gc(1, 0));
		add(new JList(), gc(1, 1));
		add(new JLabel("Destination file"), gc(1, 2));
		add(new JTextField(), gc(1, 3));
	}

	private GridBagConstraints gc(int x, int y) {
		return new GridBagConstraints(x, y, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 5, 5);
	}
}
