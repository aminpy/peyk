package org.oruji.peyk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.apache.log4j.Logger;

public class OnlineBroadCast implements Runnable {
	Logger log = Logger.getLogger(OnlineBroadCast.class.getName());

	private int port;

	public OnlineBroadCast(int port) {
		this.port = port;
	}

	public void run() {
		while (true) {
			DatagramSocket datagramSocket;
			try {
				datagramSocket = new DatagramSocket();
				datagramSocket.setBroadcast(true);

				PeykUser user = new PeykUser(port);

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
								sendData.length, broadcast, 8180);
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
				Thread.sleep(5000);

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
}
