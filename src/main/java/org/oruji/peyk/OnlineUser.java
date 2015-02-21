package org.oruji.peyk;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
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
			try {
				userSet = new Vector<PeykUser>();
				for (int i = 1; i <= 255; i++) {
					PeykUser user = new PeykUser("192.168.1." + i, port);
					if (user.getHost().equals(getMyIp()))
						continue;
					if (user.isOnline()) {
						userSet.add(user);
					}
				}

				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
	}

	public void start() {
		new Thread(this).start();
	}

	public String getMyIp() throws SocketException {
		String myIp = null;
		for (Enumeration<NetworkInterface> en = NetworkInterface
				.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface netInterface = en.nextElement();
			if (netInterface.getName().equals("wlan0")
					|| netInterface.getName().equals("eth0")) {
				for (InetAddress address : Collections.list(netInterface
						.getInetAddresses())) {
					if (address instanceof Inet4Address)
						myIp = address.toString().substring(1);
				}
			}
		}
		return myIp;
	}
}