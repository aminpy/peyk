package org.oruji.peyk;

import java.util.Vector;

public class OnlineUser implements Runnable {

	private int port;

	public OnlineUser(int port) {
		this.port = port;
	}

	public Vector<PeykUser> userSet = new Vector<PeykUser>();

	public Vector<PeykUser> getUserSet() {
		return userSet;
	}

	public void run() {
		while (true) {
			userSet = new Vector<PeykUser>();
			for (int i = 1; i <= 255; i++) {
				PeykUser user = new PeykUser("192.168.1." + i, port);
				if (user.isOnline()) {
					userSet.add(user);
				}
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void start() {
		new Thread(this).start();
	}
}