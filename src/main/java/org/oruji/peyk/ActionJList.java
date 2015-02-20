package org.oruji.peyk;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;

public class ActionJList extends MouseAdapter {
	protected JList<PeykUser> list;

	public ActionJList(JList<PeykUser> list) {
		this.list = list;
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			int index = list.locationToIndex(e.getPoint());
			ListModel<PeykUser> dlm = list.getModel();
			Object item = dlm.getElementAt(index);
			list.ensureIndexIsVisible(index);

			JFrame mainFrame = new JFrame(item.toString());
			mainFrame.setSize(600, 300);
			mainFrame.setLayout(new GridLayout(1, 1));
			mainFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent windowEvent) {
					System.exit(0);
				}
			});

			JPanel controlPanel = new JPanel();
			controlPanel.setLayout(new FlowLayout());

			mainFrame.add(controlPanel);
			mainFrame.setVisible(true);

			final JTextArea inputArea = new JTextArea(3, 40);
			final JTextArea histArea = new JTextArea(10, 50);
			histArea.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(inputArea);
			JScrollPane scrollPane2 = new JScrollPane(histArea);
			JButton showButton = new JButton("Send");

			showButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					histArea.setText(histArea.getText() + "\n"
							+ inputArea.getText());
					inputArea.setText("");
				}
			});

			controlPanel.add(scrollPane2);
			controlPanel.add(scrollPane);
			controlPanel.add(showButton);
			mainFrame.setVisible(true);
		}
	}
}