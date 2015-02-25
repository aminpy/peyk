package org.oruji.peyk;

public class Main {
	public static void main(String[] args) {
		final int port = 8180;
		GreetingServer server = new GreetingServer(port);
		server.start();

		new PeykFrame(port);
	}
}
