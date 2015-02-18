package org.oruji.peyk;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Please Enter Listening Port: ");
		int port = scanner.nextInt();

		GreetingServer server = new GreetingServer(new PeykUser(port));
		server.start();

		System.out.print("Please Enter Host: ");
		String host = scanner.next();
		GreetingClient client = new GreetingClient(new PeykUser(host, port));
		client.start();
	}
}
