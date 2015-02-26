package org.oruji.peyk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
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

	Set<PeykUser> onlineUsers = new CopyOnWriteArraySet<PeykUser>();
	Set<PeykUser> tempUsers = new CopyOnWriteArraySet<PeykUser>();

	private int port;

	public OnlineBroadCast(int port, Set<PeykUser> onlineUsers,
			Set<PeykUser> tempUsers) {
		this.port = port;
		this.onlineUsers = onlineUsers;
		this.tempUsers = tempUsers;
	}

	public void run() {
		while (true) {
			DatagramSocket datagramSocket;

			try {
				datagramSocket = new DatagramSocket();
				datagramSocket.setBroadcast(true);

				PeykUser user = new PeykUser(getMyAddress(), port);

				byte[] sendData = serialize(user);

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
								sendData.length, broadcast, port);
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
				onlineUsers.clear();
				onlineUsers.addAll(tempUsers);
				tempUsers.clear();
				System.out.println(onlineUsers);
				Thread.sleep(3000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void start() {
		new Thread(this, "Broadcast Thread").start();
	}

	public static byte[] serialize(Object obj) {
		ByteArrayOutputStream out = null;
		ObjectOutputStream os = null;

		try {
			out = new ByteArrayOutputStream();
			os = new ObjectOutputStream(out);
			os.writeObject(obj);

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if (out != null)
					out.close();

				if (os != null)
					os.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return out.toByteArray();
	}

	public static String getMyAddress() {
		Enumeration<NetworkInterface> interfaces;
		String myAddress = null;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();

			while (interfaces.hasMoreElements()) {
				NetworkInterface netInter = interfaces.nextElement();

				if (!netInter.getName().equals("wlan0")
						&& !netInter.getName().equals("eth0"))
					continue;

				if (netInter.isLoopback() || !netInter.isUp()) {
					continue;
				}

				for (InterfaceAddress interAdd : netInter
						.getInterfaceAddresses()) {

					if (interAdd.getAddress() instanceof Inet6Address)
						continue;

					InetAddress address = interAdd.getAddress();
					myAddress = address.toString().substring(1);
				}
			}

		} catch (SocketException e) {
			e.printStackTrace();
		}

		return myAddress;
	}

}
