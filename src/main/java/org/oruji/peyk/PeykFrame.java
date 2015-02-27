package org.oruji.peyk;

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

	public PeykFrame(int port, final Set<PeykUser> onlineUsers) {
		setTitle("Peyk Messenger");
		JPanel panel = new JPanel();

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
				System.out.println(text.getText());
				text.transferFocus();
			}
		});

		panel.add(text);
		panel.add(new JScrollPane(userJList));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(panel, "Center");
		setSize(300, 650);
		setResizable(false);
		setVisible(true);

		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				userJList.setListData(onlineUsers
						.toArray(new PeykUser[onlineUsers.size()]));
			}
		};

		Timer timer = new Timer(3000, taskPerformer);
		timer.setRepeats(true);
		timer.start();
	}
}
