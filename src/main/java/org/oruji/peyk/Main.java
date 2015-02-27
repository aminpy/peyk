package org.oruji.peyk;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Main {
	public static void main(String[] args) {
		Set<PeykUser> onlineUsers = new CopyOnWriteArraySet<PeykUser>();
		Set<PeykUser> tempUsers = new CopyOnWriteArraySet<PeykUser>();

		new GreetingServer().start();
		new OnlineListenUdp(tempUsers).start();
		new OnlineBroadCast(onlineUsers, tempUsers).start();
		new PeykFrame(onlineUsers);
	}
}
