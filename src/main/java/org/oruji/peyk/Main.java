package org.oruji.peyk;

public class Main {
	public static void main(String[] args) {

		final int port = 8180;

		new GreetingServer(port).start();

		new PeykFrame(port);
	}
}
