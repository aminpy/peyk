package org.oruji.peyk.main;

import org.oruji.peyk.MessageServer;
import org.oruji.peyk.OnlineClient;
import org.oruji.peyk.OnlineServer;
import org.oruji.peyk.PeykFrame;
import org.oruji.peyk.PeykTray;

public class Main {
	public static void main(String[] args) {
		PeykTray.getTray();

		new OnlineServer().start();
		new OnlineClient().start();

		new MessageServer().start();
		PeykFrame.getFrame();
	}
}
