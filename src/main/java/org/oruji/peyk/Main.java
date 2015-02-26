package org.oruji.peyk;

import java.util.HashSet;
import java.util.Set;

public class Main {
	public static void main(String[] args) {
		final int port = 8180;

		Set<PeykUser> peykUsers = new HashSet<PeykUser>();

		new GreetingServer(port).start();
		new OnlineListenUdp(port, peykUsers).start();

		new OnlineBroadCast(port).start();

		new PeykFrame(port);
	}
}
