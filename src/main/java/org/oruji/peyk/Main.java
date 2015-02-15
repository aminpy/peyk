package org.oruji.peyk;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Please Enter Listening Port: ");
		int port = scanner.nextInt();
		System.out.print("Please Enter Host: ");
		String host = scanner.next();

		GreetingServer server = new GreetingServer(port);
		GreetingClient client = new GreetingClient(host, port);

		server.start();
		client.start();
	}
}
