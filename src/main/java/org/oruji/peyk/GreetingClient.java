package org.oruji.peyk;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GreetingClient {
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		System.out.print("Please Enter Port: ");
		int port = scanner.nextInt();
		System.out.print("Please Enter Host: ");
		String server = scanner.next();

		Scanner scanIn = null;

		while (true) {
			try {
				Socket client = new Socket(server, port);

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
}
