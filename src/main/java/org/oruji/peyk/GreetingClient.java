package org.oruji.peyk;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class GreetingClient {
	public static void main(String[] args) {
		String server = "localhost";
		int port = 8180;
		System.out.println("Connecting to " + server + ":" + port);

		try {
			Socket client = new Socket(server, port);
			System.out
					.println("connected to" + client.getRemoteSocketAddress());

			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);

			out.writeUTF("Hello From: " + client.getLocalSocketAddress());
			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			System.out.println("Server says" + in.readUTF());
			client.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
