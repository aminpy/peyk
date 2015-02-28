package org.oruji.peyk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;

public class OnlineBroadCast implements Runnable {
	Logger log = Logger.getLogger(OnlineBroadCast.class.getName());

	Set<PeykUser> tempUsers = new CopyOnWriteArraySet<PeykUser>();

	public OnlineBroadCast(Set<PeykUser> tempUsers) {
		this.tempUsers = tempUsers;
	}

	public void run() {
		while (true) {
			DatagramSocket datagramSocket;

			try {
				datagramSocket = new DatagramSocket();
				datagramSocket.setBroadcast(true);

				byte[] sendData = PeykUser.getSourceUser().serialize();

				Enumeration<NetworkInterface> interfaces = NetworkInterface
						.getNetworkInterfaces();

				while (interfaces.hasMoreElements()) {
					NetworkInterface netInter = interfaces.nextElement();

					if (netInter.isLoopback() || !netInter.isUp()) {
						continue;
					}

					for (InterfaceAddress interAdd : netInter
							.getInterfaceAddresses()) {
						InetAddress broadcast = interAdd.getBroadcast();

						if (broadcast == null)
							continue;

						log.info("broadcast: " + broadcast);
						DatagramPacket packet = new DatagramPacket(sendData,
								sendData.length, broadcast, PeykUser
										.getSourceUser().getPort());
						datagramSocket.send(packet);
					}
				}

			} catch (SocketException e) {
				e.printStackTrace();

			} catch (UnknownHostException e) {
				e.printStackTrace();

			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				PeykUser sourceUser = PeykUser.getSourceUser();

				sourceUser.getFriendsList().clear();
				sourceUser.getFriendsList().addAll(tempUsers);
				tempUsers.clear();
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
