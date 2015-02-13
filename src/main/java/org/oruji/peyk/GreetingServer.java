package org.oruji.peyk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class GreetingServer extends Thread {
	private ServerSocket serverSocket;

	public GreetingServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("wait clint port: "
						+ serverSocket.getLocalPort());
				Socket server = serverSocket.accept();
				System.out.println("connected to: "
						+ server.getRemoteSocketAddress());
				DataInputStream in = new DataInputStream(
						server.getInputStream());
				System.out.println(in.readUTF());
				DataOutputStream out = new DataOutputStream(
						server.getOutputStream());
				out.writeUTF("thanks connecting to: "
						+ server.getLocalSocketAddress() + "\nGoodbye!");
				server.close();
			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public static void main(String[] args) {
		try {
			Thread t = new GreetingServer(8180);
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
