package org.oruji.peyk;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class FileChooser extends JFrame {
	private static final long serialVersionUID = 1L;

	public FileChooser() {
		JPanel panel = new JPanel();
		Container container = getContentPane();
		container.add(panel, BorderLayout.SOUTH);
		panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		container.add(panel, BorderLayout.NORTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(250, 110);
		setVisible(true);
	}
}
