package org.oruji.peyk;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GreetingServer implements Runnable {

	private int port;

	public GreetingServer(int port) {
		this.port = port;
	}

	public void run() {
		ServerSocket serverSocket = null;
		while (true) {
			try {
				serverSocket = new ServerSocket(port);
				Socket server = serverSocket.accept();
				DataInputStream in = new DataInputStream(
						server.getInputStream());
				System.out.println(in.readUTF());
				server.close();
				serverSocket.close();
			} catch (IOException e) {
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
