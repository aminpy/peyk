package org.oruji.peyk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

import org.apache.log4j.Logger;
import org.oruji.peyk.model.PeykUser;

public class OnlineBroadCast implements Runnable {
	private Logger log = Logger.getLogger(OnlineBroadCast.class.getName());

	public void run() {
		PeykUser sourceUser = PeykUser.getSourceUser();
		while (true) {
			DatagramSocket datagramSocket;

			try {
				datagramSocket = new DatagramSocket();
				datagramSocket.setBroadcast(true);

				byte[] sendData = sourceUser.serialize();

				InetAddress broadcast = PeykUser.getMyBroadcast();

				DatagramPacket packet = new DatagramPacket(sendData,
						sendData.length, broadcast, sourceUser.getPort());
				datagramSocket.send(packet);
				log.info("broadcast: " + broadcast);

			} catch (IOException e) {
				e.printStackTrace();
			}

			// remove old online users
			for (PeykUser u : sourceUser.getFriendsList()) {
				if ((new Date().getTime() - u.getReceiveDate().getTime()) > (4000)) {
					sourceUser.getFriendsList().remove(u);
				}
			}

			try {
				Thread.sleep(3000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void start() {
		new Thread(this, "Broadcast Thread").start();
	}
}
