package org.oruji.peyk;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GreetingClient implements Runnable {

	private final String host;
	private final int port;

	public GreetingClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void run() {
		Scanner scanIn = null;

		while (true) {
			try {
				Socket client = new Socket(host, port);

				OutputStream outToServer = client.getOutputStream();
				DataOutputStream out = new DataOutputStream(outToServer);

				scanIn = new Scanner(System.in);
				String inputString;
				inputString = scanIn.nextLine();
				out.writeUTF(inputString);

				client.close();

			} catch (UnknownHostException e) {
				e.printStackTrace();
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		scanIn.close();
	}

	public void start() {
		new Thread(this).start();
	}
}
