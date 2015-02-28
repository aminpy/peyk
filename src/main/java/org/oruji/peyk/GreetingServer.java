package org.oruji.peyk;

import org.apache.log4j.Logger;

public class GreetingServer implements Runnable {
	Logger log = Logger.getLogger(GreetingServer.class.getName());

	public void run() {
		while (true) {
			PeykMessage message = PeykMessage.receiveMessage(PeykUser
					.getSourceUser().getPort());

			if (message == null)
				continue;

			ChatFrame chatFrame = ChatFrame.getChatFrame(message.getSender());

			chatFrame.appendText(message.receiveFormat());
		}
	}

	public void start() {
		new Thread(this, "Server Thread").start();
	}
}
