package org.oruji.peyk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;

public class OnlineListenUdp implements Runnable {
	Logger log = Logger.getLogger(OnlineListenUdp.class.getName());

	private Set<PeykUser> peykUsers = new CopyOnWriteArraySet<PeykUser>();

	public OnlineListenUdp(Set<PeykUser> peykUsers) {
		this.peykUsers = peykUsers;
	}

	public void run() {
		DatagramSocket datagramSocket = null;
		PeykUser sourceUser = PeykUser.getSourceUser();

		while (true) {
			try {
				datagramSocket = new DatagramSocket(sourceUser.getPort());
				datagramSocket.setBroadcast(true);

				byte[] buffer = new byte[15000];
				DatagramPacket packet = new DatagramPacket(buffer,
						buffer.length);

				datagramSocket.receive(packet);
				PeykUser peykUser = PeykUser.deserialize(packet.getData());

				if (peykUser == null) {
					log.error("Listen UDP: deserialized peykUser is null !!!");

				} else {
					if (!peykUser.equals(sourceUser)) {
						log.info("Received UDP from: " + peykUser);
						peykUsers.add(peykUser);
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
