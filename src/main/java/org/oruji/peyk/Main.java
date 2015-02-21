package org.oruji.peyk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.Timer;

public class Main {
	static JList<PeykUser> userJList = null;
	static ChatFrame chatFrame = null;
	static Set<ChatFrame> openChatFrames = new HashSet<ChatFrame>();

	public static void main(String[] args) {
		final int port = 8180;
		GreetingServer server = new GreetingServer(port, openChatFrames);
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

					chatFrame = new ChatFrame(peykUser);

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
