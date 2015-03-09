package org.oruji.peyk;

public class Main {
	public static void main(String[] args) {
		PeykTray.getTray();

		new OnlineServer().start();
		new OnlineClient().start();

		new MessageServer().start();
		PeykFrame.getFrame();
	}
}
