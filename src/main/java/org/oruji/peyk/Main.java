package org.oruji.peyk;

import java.util.Scanner;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Please Enter Listening Port: ");
		int port = scanner.nextInt();
		GreetingServer server = new GreetingServer(port);
		server.start();

		OnlineUser user = new OnlineUser(port);
		user.start();

		// swing
		JFrame frame = new JFrame("Peyk Messenger");
		JPanel panel = new JPanel();
		JList<PeykUser> list = new JList<PeykUser>();
		Vector<PeykUser> data = new Vector<PeykUser>();

		for (PeykUser peykUser : user.getUserSet()) {
			data.addElement(peykUser);
		}

		list.setListData(data);

		panel.add(new JScrollPane(list));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().add(panel, "Center");
		frame.setSize(300, 650);
		frame.setVisible(true);
		// swing

		// System.out.print("Please Enter Host: ");
		// String host = scanner.next();
		// GreetingClient client = new GreetingClient(new PeykUser(host, port));
		// client.start();
	}
}
