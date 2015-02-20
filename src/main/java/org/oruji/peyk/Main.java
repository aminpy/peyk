package org.oruji.peyk;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.Timer;

public class Main {
	static JList<PeykUser> userJList = null;
	static JFrame chatFrame = null;
	static Set<JFrame> openChatFrames = new HashSet<JFrame>();

	public static void main(String[] args) {
		final int port = 8180;
		GreetingServer server = new GreetingServer(port);
		server.start();

		final OnlineUser onlineUser = new OnlineUser(port);
		onlineUser.start();

		// swing
		JFrame peykFrame = new JFrame("Peyk Messenger");
		JPanel panel = new JPanel();
		userJList = new JList<PeykUser>();
		userJList.setListData(onlineUser.getUserSet());
		userJList.setSelectedIndex(0);

		userJList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					int index = userJList.locationToIndex(e.getPoint());
					ListModel<PeykUser> userListModel = userJList.getModel();
					Object item = userListModel.getElementAt(index);

					final PeykUser peykUser = (PeykUser) item;

					userJList.ensureIndexIsVisible(index);

					if (chatFrame != null && openChatFrames.size() != 0) {
						for (JFrame ocf : openChatFrames) {
							if (ocf.getTitle().equals(peykUser.toString())) {
								ocf.setVisible(true);
								return;
							}
						}
					}

					chatFrame = new JFrame(peykUser.toString());
					// chatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					chatFrame.setSize(600, 300);
					chatFrame.setLayout(new GridLayout(1, 1));

					chatFrame.addWindowListener(new WindowAdapter() {
						public void windowClosing(WindowEvent windowEvent) {
							chatFrame.setVisible(false);
						}
					});

					JPanel controlPanel = new JPanel();
					controlPanel.setLayout(new FlowLayout());

					chatFrame.add(controlPanel);

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

							try {
								Socket client = new Socket(peykUser.getHost(),
										peykUser.getPort());
								OutputStream outToServer = client
										.getOutputStream();
								DataOutputStream out = new DataOutputStream(
										outToServer);

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
					chatFrame.setVisible(true);
					openChatFrames.add(chatFrame);
				}
			}
		});

		panel.add(new JScrollPane(userJList));

		peykFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		peykFrame.getContentPane().add(panel, "Center");
		peykFrame.setSize(300, 650);
		peykFrame.setVisible(true);

		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				userJList.setListData(onlineUser.getUserSet());
			}
		};

		Timer timer = new Timer(2000, taskPerformer);
		timer.setRepeats(true);
		timer.start();
	}
}
