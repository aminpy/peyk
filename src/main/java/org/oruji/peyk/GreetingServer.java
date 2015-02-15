package org.oruji.peyk;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class GreetingServer {

	private static Logger log = Logger
			.getLogger(GreetingServer.class.getName());

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Please Enter Listening Port: ");
		int port = scanner.nextInt();
		scanner.close();

		while (true) {
			try {
				ServerSocket serverSocket = new ServerSocket(port);
				log.info("info Listening on " + serverSocket.getLocalSocketAddress());
				Socket server = serverSocket.accept();
				DataInputStream in = new DataInputStream(
						server.getInputStream());
				System.out.println(in.readUTF());
				server.close();
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
}
