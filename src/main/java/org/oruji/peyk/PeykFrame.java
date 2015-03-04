package org.oruji.peyk;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.Timer;

public class PeykFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "Peyk Messenger";

	private static PeykFrame peykFrame = null;

	public static PeykFrame getFrame() {
		if (peykFrame == null) {
			peykFrame = new PeykFrame();
		}

		return peykFrame;
	}

	private PeykFrame() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				setVisible(false);
			}
		});

		final PeykUser sourceUser = PeykUser.getSourceUser();
		setTitle(TITLE + " - " + sourceUser.getName());

		final JList<PeykUser> userJList = new JList<PeykUser>();
		final JTextField text = new JTextField(15);
		text.setText(System.getProperty("user.name"));

		PeykUser[] onlineArray = sourceUser.getFriendsList().toArray(
				new PeykUser[sourceUser.getFriendsList().size()]);

		userJList.setListData(onlineArray);
		userJList.setSelectedIndex(0);

		userJList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = userJList.locationToIndex(e.getPoint());
					ListModel<PeykUser> userListModel = userJList.getModel();

					if (userListModel.getSize() != 0) {

						Object item = userListModel.getElementAt(index);

						final PeykUser selectedUser = (PeykUser) item;

						userJList.ensureIndexIsVisible(index);
						ChatFrame.getChatFrame(selectedUser);
					}
				}
			}
		});

		text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String content = text.getText().trim();

				if (content.equals("")) {
					sourceUser.setName(System.getProperty("user.name"));
					text.setText(System.getProperty("user.name"));

				} else
					sourceUser.setName(text.getText());

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
				userJList.setListData(sourceUser.getFriendsList().toArray(
						new PeykUser[sourceUser.getFriendsList().size()]));
				setTitle(TITLE + " - " + sourceUser.getName());

			}
		};

		Timer timer = new Timer(3000, taskPerformer);
		timer.setRepeats(true);
		timer.start();

		setSize(300, 650);
		setResizable(true);
		setVisible(true);
		text.transferFocus();
	}
}
