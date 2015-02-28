package org.oruji.peyk;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class ChatFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextArea histArea = null;
	private final PeykUser destUser;
	private static Set<ChatFrame> chatFrames = new HashSet<ChatFrame>();

	public static ChatFrame getChatFrame(PeykUser destUser) {
		for (ChatFrame cf : chatFrames) {
			if (cf.toString().equals(destUser.toStringUnique())) {
				cf.setVisible(true);
				return cf;
			}
		}

		ChatFrame chatFrame = new ChatFrame(destUser);
		chatFrames.add(chatFrame);

		return chatFrame;
	}

	private ChatFrame(final PeykUser destUser) {
		this.destUser = destUser;
		setTitle(destUser.toString());
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

		final JTextField inputArea = new JTextField(40);
		histArea = new JTextArea(10, 50);
		histArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(inputArea);
		JScrollPane scrollPane2 = new JScrollPane(histArea);
		JButton showButton = new JButton("Send");

		showButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				histArea.setText(histArea.getText().equals("") ? "me: "
						+ inputArea.getText() : histArea.getText() + "\n"
						+ "me: " + inputArea.getText());

				destUser.setMessage(inputArea.getText());
				PeykMessage.sendMessage(destUser);

				inputArea.setText("");
			}
		});

		controlPanel.add(scrollPane2);
		controlPanel.add(scrollPane);
		controlPanel.add(showButton);

		JRootPane rootPane = getRootPane();
		rootPane.setDefaultButton(showButton);

		DefaultCaret caret = (DefaultCaret) histArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		histArea.setFocusable(false);

		setResizable(false);
		setVisible(true);
	}

	public void appendText(String text) {
		if (histArea == null)
			histArea = new JTextArea(10, 50);

		histArea.setText(histArea.getText().equals("") ? text : histArea
				.getText() + "\n" + text);

	}

	@Override
	public String toString() {
		return destUser.toStringUnique();
	}
}
