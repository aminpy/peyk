package org.oruji.peyk;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class ChatFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private JEditorPane chatPanel = null;
	private HTMLDocument doc = null;
	private HTMLEditorKit kit = null;

	private final PeykUser destUser;
	private static Set<ChatFrame> chatFrames = new HashSet<ChatFrame>();

	public static ChatFrame getChatFrame(PeykUser destUser) {
		for (ChatFrame chatFrame : chatFrames) {
			if (chatFrame.toString().equals(destUser.toStringUnique())) {
				chatFrame.setVisible(true);
				return chatFrame;
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

		doc = new HTMLDocument();
		kit = new HTMLEditorKit();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				setVisible(false);
			}
		});

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());

		add(controlPanel);

		final JTextField inputArea = new JTextField(40);
		chatPanel = new JEditorPane();
		Dimension preferred = new Dimension(550, 200);
		chatPanel.setPreferredSize(preferred);
		chatPanel.setMaximumSize(preferred);
		chatPanel.setContentType("text/html");

		JScrollPane scroll = new JScrollPane(chatPanel);
		scroll.setPreferredSize(preferred);
		scroll.setMaximumSize(preferred);
		JButton showButton = new JButton("Send");

		showButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PeykMessage peykMessage = new PeykMessage();
				peykMessage.setSendDate(new Date());
				peykMessage.setText(inputArea.getText());
				peykMessage.setSender(PeykUser.getSourceUser());
				peykMessage.setReceiver(destUser);

				String text = peykMessage.sendFormat();

				appendText(text);

				PeykMessage.sendMessage(peykMessage);

				inputArea.setText("");
			}
		});

		controlPanel.add(scroll);
		controlPanel.add(inputArea);
		controlPanel.add(showButton);

		JRootPane rootPane = getRootPane();
		rootPane.setDefaultButton(showButton);

		DefaultCaret caret = (DefaultCaret) chatPanel.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		chatPanel.setFocusable(false);

		setResizable(false);
		setVisible(true);
	}

	public void appendText(String text) {
		try {
			chatPanel.setEditorKit(kit);
			chatPanel.setDocument(doc);
			kit.insertHTML(doc, doc.getLength(), text, 0, 0, null);

		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return destUser.toStringUnique();
	}
}
