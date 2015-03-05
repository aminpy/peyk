package org.oruji.peyk;


public class Main {
	public static void main(String[] args) {
		PeykTray.getTray();

		new OnlineListenUdp().start();
		new OnlineBroadCast().start();

		new GreetingServer().start();
		PeykFrame.getFrame();
	}
}
