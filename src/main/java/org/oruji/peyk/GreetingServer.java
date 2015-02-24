package org.oruji.peyk;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class GreetingServer implements Runnable {
	Set<ChatFrame> openChatFrames = new HashSet<ChatFrame>();
	private int port;
	Logger log = Logger.getLogger(GreetingServer.class.getName());

	public GreetingServer(int port, Set<ChatFrame> openChatFrames) {
		this.port = port;
		this.openChatFrames = openChatFrames;
	}

	public void run() {
		ServerSocket serverSocket = null;
		Socket server = null;
		DataInputStream in = null;
		while (true) {
			try {
				serverSocket = new ServerSocket(port);
				log.info("before server accept <<<<<<<<<<<<<<<");
				server = serverSocket.accept();
				log.info("after  server accept >>>>>>>>>>>>>>>");

				in = new DataInputStream(server.getInputStream());
				String receivedStr = in.readUTF();

				if (receivedStr.equals("[[[[ping]]]]"))
					continue;

				String host = server.toString().split("=")[1].split(",port")[0]
						.substring(1);
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
			} catch (IOException e) {
				if (e instanceof EOFException) {
					log.error("EOF Exception occured !");
				}

				continue;
			} finally {
				try {
					if (serverSocket != null)
						serverSocket.close();
					if (server != null)
						server.close();
					if (in != null)
						in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public void start() {
		new Thread(this).start();
	}
}
