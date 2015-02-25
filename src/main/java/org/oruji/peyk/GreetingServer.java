package org.oruji.peyk;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class GreetingServer implements Runnable {
	private int port;
	Logger log = Logger.getLogger(GreetingServer.class.getName());

	public GreetingServer(int port) {
		this.port = port;
	}

	public void run() {
		ServerSocket serverSocket = null;
		Socket server = null;
		ObjectInputStream in = null;
		while (true) {
			try {
				serverSocket = new ServerSocket(port);
				log.info("before server accept <<<<<<<<<<<<<<<");
				server = serverSocket.accept();
				log.info("after  server accept >>>>>>>>>>>>>>>");

				InputStream inputStream = server.getInputStream();

				in = new ObjectInputStream(inputStream);
				Object inputObj = in.readObject();

				if (inputObj instanceof PeykUser) {
					PeykUser peykUser = (PeykUser) inputObj;
					continue;

				} else if (inputObj instanceof String) {
					String host = server.toString().split("=")[1]
							.split(",port")[0].substring(1);
					PeykUser peykUser = new PeykUser(host, port);

					ChatFrame chatFrame = ChatFrame.getChatFrame(peykUser);

					chatFrame.appendText((String) inputObj);
				}

			} catch (IOException e) {
				if (e instanceof EOFException) {
					log.error("EOF Exception occured !");
				}

				continue;

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
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
		new Thread(this, "Server Thread").start();
	}
}
