package org.oruji.peyk;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

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

	public static void main(String[] args) {
		final int port = 8180;
		GreetingServer server = new GreetingServer(port);
		server.start();

		final OnlineUser onlineUser = new OnlineUser(port);
		onlineUser.start();

		// swing
		JFrame frame = new JFrame("Peyk Messenger");
		JPanel panel = new JPanel();
		userJList = new JList<PeykUser>();
		userJList.setListData(onlineUser.getUserSet());
		userJList.setSelectedIndex(0);

		userJList.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = userJList.locationToIndex(e.getPoint());
					ListModel<PeykUser> userListModel = userJList.getModel();
					Object item = userListModel.getElementAt(index);

					final PeykUser peykUser = item instanceof PeykUser ? (PeykUser) item
							: null;

					userJList.ensureIndexIsVisible(index);

					JFrame mainFrame = new JFrame(peykUser.toString());
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
					mainFrame.setVisible(true);
				}
			}
		});

		panel.add(new JScrollPane(userJList));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel, "Center");
		frame.setSize(300, 650);
		frame.setVisible(true);

		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				userJList.setListData(onlineUser.getUserSet());
			}
		};

		Timer timer = new Timer(2000, taskPerformer);
		timer.setRepeats(true);
		timer.start();
		// swing

		// System.out.print("Please Enter Host: ");
		// String host = scanner.next();
		// GreetingClient client = new GreetingClient(new PeykUser(host, port));
		// client.start();
	}
}
