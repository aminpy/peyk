package org.oruji.peyk;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class GreetingClient implements Runnable {

	private static Logger log = Logger
			.getLogger(GreetingClient.class.getName());
	private final String host;
	private final int port;

	public GreetingClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void run() {
		Scanner scanIn = null;

		String inputString = null;
		while (true) {
			try {
				Socket client = new Socket(host, port);

				OutputStream outToServer = client.getOutputStream();
				DataOutputStream out = new DataOutputStream(outToServer);

				scanIn = new Scanner(System.in);
				if (inputString == null)
					inputString = scanIn.nextLine();
				out.writeUTF(inputString);

				client.close();
				inputString = null;

			} catch (UnknownHostException e) {
				e.printStackTrace();
				break;
			} catch (IOException e) {
//				log.error(e.getMessage());
				continue;
			}
		}
		scanIn.close();
	}

	public void start() {
		new Thread(this).start();
	}
}
