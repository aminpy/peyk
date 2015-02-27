package org.oruji.peyk;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.Timer;

public class PeykFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public PeykFrame(final PeykUser serverUser, final Set<PeykUser> onlineUsers) {
		setTitle("Peyk Messenger");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JList<PeykUser> userJList = new JList<PeykUser>();
		final JTextField text = new JTextField(15);

		PeykUser[] onlineArray = onlineUsers.toArray(new PeykUser[onlineUsers
				.size()]);

		userJList.setListData(onlineArray);
		userJList.setSelectedIndex(0);

		userJList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = userJList.locationToIndex(e.getPoint());
					ListModel<PeykUser> userListModel = userJList.getModel();
					Object item = userListModel.getElementAt(index);

					final PeykUser peykUser = (PeykUser) item;

					userJList.ensureIndexIsVisible(index);

					ChatFrame.getChatFrame(peykUser);
				}
			}
		});

		text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serverUser.setName(text.getText());
				text.transferFocus();
			}
		});

		JScrollPane listScroll = new JScrollPane(userJList);

		Dimension d = userJList.getPreferredSize();
		d.width = 200;
		d.height = 200;
		listScroll.setPreferredSize(d);

		JPanel panel = new JPanel();
		panel.add(text);
		panel.add(listScroll);

		getContentPane().add(panel, "Center");

		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				userJList.setListData(onlineUsers
						.toArray(new PeykUser[onlineUsers.size()]));
			}
		};

		Timer timer = new Timer(3000, taskPerformer);
		timer.setRepeats(true);
		timer.start();

		setSize(300, 650);
		setResizable(true);
		setVisible(true);
	}
}
