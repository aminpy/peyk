package org.oruji.peyk;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Main {
	public static void main(String[] args) {
		Set<PeykUser> tempUsers = new CopyOnWriteArraySet<PeykUser>();

		PeykTray.getTray();

		new OnlineListenUdp(tempUsers).start();
		new OnlineBroadCast(tempUsers).start();

		new GreetingServer().start();
		PeykFrame.getFrame();
	}
}
