package org.oruji.peyk;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Main {
	public static void main(String[] args) {
		final int port = 8180;
		PeykUser sourceUser = new PeykUser(OnlineBroadCast.getMyAddress(), port);
		sourceUser.setName(System.getProperty("user.name"));
		Set<PeykUser> onlineUsers = new CopyOnWriteArraySet<PeykUser>();
		Set<PeykUser> tempUsers = new CopyOnWriteArraySet<PeykUser>();

		new GreetingServer(port).start();
		new OnlineListenUdp(port, tempUsers).start();
		new OnlineBroadCast(sourceUser, onlineUsers, tempUsers).start();
		new PeykFrame(sourceUser, onlineUsers);
	}
}
