package org.oruji.peyk;

import java.util.HashSet;
import java.util.Set;

public class OnlineUser implements Runnable {

	private int port;

	public OnlineUser(int port) {
		this.port = port;
	}

	public Set<PeykUser> userSet = new HashSet<PeykUser>();

	public Set<PeykUser> getUserSet() {
		return userSet;
	}

	public void run() {
		for (int i = 1; i <= 255; i++) {
			PeykUser user = new PeykUser("192.168.1." + i, port);
			if (user.isOnline()) {
				userSet.add(user);
			}
		}
		System.out.println(userSet);
		System.out.println();
	}

	public void start() {
		new Thread(this).start();
	}
}
