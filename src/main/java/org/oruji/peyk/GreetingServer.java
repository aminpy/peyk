package org.oruji.peyk;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class GreetingServer implements Runnable {
	Set<ChatFrame> openChatFrames = new HashSet<ChatFrame>();
	private int port;

	public GreetingServer(int port, Set<ChatFrame> openChatFrames) {
		this.port = port;
		this.openChatFrames = openChatFrames;
	}

	public void run() {
		ServerSocket serverSocket = null;
		while (true) {
			try {
				serverSocket = new ServerSocket(port);
				Socket server = serverSocket.accept();

				String host = server.toString().split("=")[1].split(",port")[0]
						.substring(1);

				DataInputStream in = new DataInputStream(
						server.getInputStream());
				String receivedStr = in.readUTF();
				System.out.println(receivedStr);

				PeykUser peykUser = new PeykUser(host, port);

				ChatFrame chatFrame = null;
				for (ChatFrame chat : openChatFrames) {
					if (chat.getTitle().equals(peykUser.toString())) {
						chatFrame = chat;
						chatFrame.setVisible(true);
						break;
					}
				}

				if (chatFrame == null) {
					chatFrame = new ChatFrame(peykUser);
				}

				chatFrame.appendText(receivedStr);
				openChatFrames.add(chatFrame);

				server.close();
				serverSocket.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				continue;
			} finally {
				if (serverSocket != null) {
					try {
						serverSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void start() {
		new Thread(this).start();
	}
}
