package org.oruji.peyk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;

public class OnlineBroadCast implements Runnable {
	private Logger log = Logger.getLogger(OnlineBroadCast.class.getName());

	private Set<PeykUser> tempUsers = new CopyOnWriteArraySet<PeykUser>();

	public OnlineBroadCast(Set<PeykUser> tempUsers) {
		this.tempUsers = tempUsers;
	}

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

			sourceUser.getFriendsList().clear();
			sourceUser.getFriendsList().addAll(tempUsers);
			tempUsers.clear();

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
