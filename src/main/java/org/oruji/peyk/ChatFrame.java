package org.oruji.peyk;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChatFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private JTextArea histArea = null;

	public ChatFrame(final PeykUser peykUser) {
		setTitle(peykUser.toString());
		setSize(600, 300);
		setLayout(new GridLayout(1, 1));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				setVisible(false);
			}
		});

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());

		add(controlPanel);

		final JTextArea inputArea = new JTextArea(3, 40);
		histArea = new JTextArea(10, 50);
		histArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(inputArea);
		JScrollPane scrollPane2 = new JScrollPane(histArea);
		JButton showButton = new JButton("Send");

		showButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// histArea.setText(histArea.getText() + "\n"
				// + inputArea.getText());
				histArea.setText(histArea.getText().equals("") ? inputArea
						.getText() : histArea.getText() + "\n"
						+ inputArea.getText());

				try {
					Socket client = new Socket(peykUser.getHost(), peykUser
							.getPort());
					OutputStream outToServer = client.getOutputStream();
					DataOutputStream out = new DataOutputStream(outToServer);

					out.writeUTF(inputArea.getText());

					client.close();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				inputArea.setText("");
			}
		});

		controlPanel.add(scrollPane2);
		controlPanel.add(scrollPane);
		controlPanel.add(showButton);
		setVisible(true);
	}

	public void appendText(String text) {
		if (histArea == null)
			histArea = new JTextArea(10, 50);
		histArea.setText(histArea.getText() + "\n" + text);
	}
}
