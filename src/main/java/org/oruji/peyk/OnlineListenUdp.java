package org.oruji.peyk;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
		while (true) {
			try {
				datagramSocket = new DatagramSocket(PeykUser.getSourceUser()
						.getPort());
				datagramSocket.setBroadcast(true);

				byte[] buffer = new byte[15000];
				DatagramPacket packet = new DatagramPacket(buffer,
						buffer.length);

				datagramSocket.receive(packet);
				Object obj = deserialize(packet.getData());

				if (obj instanceof PeykUser) {
					PeykUser peykUser = (PeykUser) obj;
					if (!peykUser.equals(PeykUser.getSourceUser())) {
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

	public static Object deserialize(byte[] data) {
		ByteArrayInputStream in = null;
		ObjectInputStream is = null;
		Object obj = null;

		try {
			in = new ByteArrayInputStream(data);
			is = new ObjectInputStream(in);
			obj = is.readObject();

		} catch (IOException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		} finally {
			try {
				if (in != null)
					in.close();

				if (is != null)
					is.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return obj;
	}
}
