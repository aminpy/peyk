package org.oruji.peyk;

import org.apache.log4j.Logger;

public class GreetingServer implements Runnable {
	Logger log = Logger.getLogger(GreetingServer.class.getName());

	public void run() {
		while (true) {
			PeykUser destUser = PeykMessage.receiveMessage(PeykUser.getSourceUser().getPort());

			if (destUser == null)
				continue;

			ChatFrame chatFrame = ChatFrame.getChatFrame(destUser);

			chatFrame.appendText(destUser.getName() + ": "
					+ destUser.getMessage());
		}
	}

	public void start() {
		new Thread(this, "Server Thread").start();
	}
}
