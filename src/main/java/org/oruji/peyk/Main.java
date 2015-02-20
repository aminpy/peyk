package org.oruji.peyk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

public class Main {
	static JList<PeykUser> list = null;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Please Enter Listening Port: ");
		final int port = scanner.nextInt();
		GreetingServer server = new GreetingServer(port);
		server.start();

		// swing
		JFrame frame = new JFrame("Peyk Messenger");
		JPanel panel = new JPanel();
		list = new JList<PeykUser>();

		list.setListData(getOnlineUsers(port));

		panel.add(new JScrollPane(list));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().add(panel, "Center");
		frame.setSize(300, 650);
		frame.setVisible(true);

		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				list.setListData(getOnlineUsers(port));
			}
		};

		Timer timer = new Timer(2000, taskPerformer);
		timer.setRepeats(true);
		timer.start();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// swing

		// System.out.print("Please Enter Host: ");
		// String host = scanner.next();
		// GreetingClient client = new GreetingClient(new PeykUser(host, port));
		// client.start();
	}

	private static Vector<PeykUser> getOnlineUsers(int port) {
		Vector<PeykUser> userList = new Vector<PeykUser>();

		for (int i = 1; i <= 255; i++) {
			PeykUser peykUser = new PeykUser("192.168.1." + i, port);
			if (peykUser.isOnline()) {
				userList.addElement(peykUser);
			}
		}

		return userList;
	}
}
