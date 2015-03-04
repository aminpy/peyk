package org.oruji.peyk;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.log4j.Logger;

public class ChatFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(ChatFrame.class.getName());

	private JTextPane chatPanel = null;
	private HTMLDocument doc = null;
	private HTMLEditorKit kit = null;
	private JPanel panel = null;
	public JScrollPane scroll = null;

	private static Set<ChatFrame> chatFrames = new HashSet<ChatFrame>();

	private final PeykUser destUser;

	private final Dimension CHAT_SIZE = new Dimension(550, 200);

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
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				setVisible(false);
			}
		});

		this.destUser = destUser;

		setTitle(destUser.toString());
		setSize(600, 300);

		doc = new HTMLDocument();
		kit = new HTMLEditorKit();

		final JTextField inputArea = new JTextField(40);
		chatPanel = new JTextPane();
		chatPanel.setFocusable(false);
		chatPanel.setPreferredSize(CHAT_SIZE);
		chatPanel.setMaximumSize(CHAT_SIZE);
		chatPanel.setContentType("text/html");

		scroll = new JScrollPane(chatPanel);
		scroll.setPreferredSize(CHAT_SIZE);
		scroll.setMaximumSize(CHAT_SIZE);

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

		panel = new JPanel();
		setContentPane(panel);
		panel.add(scroll);
		panel.add(inputArea);
		panel.add(showButton);

		JRootPane rootPane = getRootPane();
		rootPane.setDefaultButton(showButton);

		setResizable(false);
		setVisible(true);
	}

	public void appendText(String text) {
		try {
			chatPanel.setEditorKit(kit);
			chatPanel.setDocument(doc);

			// auto scrolling
			chatPanel.setCaretPosition(chatPanel.getDocument().getLength());
			text = TextFormatter.buildStr(text);
			text = TextFormatter.emoticons(text);
			kit.insertHTML(doc, doc.getLength(), text, 0, 0, null);
			log.info(text);

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
