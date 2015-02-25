package org.oruji.peyk;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

public class OnlineUser {

	private Set<String> nodes = new HashSet<String>();
	private int port;

	public OnlineUser(int port) {
		this.port = port;
	}

	private Set<String> getNodes() {
		for (int i = 2; i < 10; i++) {
			String host = "192.168.1." + i;
			if (isReachable(host)) {
				nodes.add(host);
			}
		}

		return nodes;
	}

	public boolean isReachable(final String host) {
		try {
			Runnable runn = new Runnable() {
				public void run() {
					
				}
			};

			TimeoutController.execute(runn, 30);
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}

	public Vector<PeykUser> getOnlineUsers() {
		Vector<PeykUser> onlineUsers = new Vector<PeykUser>();

		getNodes();

		for (String node : nodes) {
			PeykUser user = new PeykUser(node, port);
			if (user.isOnline()) {
				onlineUsers.add(user);
			}
		}

		return onlineUsers;
	}
}
