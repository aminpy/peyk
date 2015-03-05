package org.oruji.peyk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;

import org.apache.log4j.Logger;

public class OnlineListenUdp implements Runnable {
	Logger log = Logger.getLogger(OnlineListenUdp.class.getName());

	public void run() {
		DatagramSocket datagramSocket = null;
		PeykUser sourceUser = PeykUser.getSourceUser();

		while (true) {
			try {
				datagramSocket = new DatagramSocket(PeykUser.getSourceUser()
						.getPort());
				datagramSocket.setBroadcast(true);

				byte[] buffer = new byte[15000];
				DatagramPacket packet = new DatagramPacket(buffer,
						buffer.length);

				datagramSocket.receive(packet);
				PeykUser receivedUser = PeykUser.deserialize(packet.getData());

				if (receivedUser == null) {
					log.error("Listen UDP: deserialized peykUser is null !!!");

				} else {
					if (!receivedUser.equals(sourceUser)) {
						log.info("Received UDP from: " + receivedUser);

						// add or replace new online user
						receivedUser.setReceiveDate(new Date());

						if (PeykUser.getSourceUser().getFriendsList()
								.contains(receivedUser)) {
							PeykUser.getSourceUser().getFriendsList()
									.remove(receivedUser);
							PeykUser.getSourceUser().getFriendsList()
									.add(receivedUser);

						} else {
							PeykUser.getSourceUser().getFriendsList()
									.add(receivedUser);
						}
					}
				}

			} catch (SocketException e) {
				e.printStackTrace();

			} catch (IOException e) {
				e.printStackTrace();

			} finally {
				datagramSocket.close();
			}
		}
	}

	public void start() {
		new Thread(this, "Server UDP Thread").start();
	}
}
