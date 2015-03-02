package org.oruji.peyk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;

public final class PeykUser implements Serializable {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(PeykUser.class.getName());

	private String name;
	private String host;
	private final int port = 8180;
	private Set<PeykUser> friendsList = new CopyOnWriteArraySet<PeykUser>();

	private static PeykUser sourceUser = null;

	public static PeykUser getSourceUser() {
		if (sourceUser != null)
			return sourceUser;

		sourceUser = new PeykUser(getMyAddress());
		sourceUser.setName(System.getProperty("user.name"));

		return sourceUser;
	}

	public PeykUser(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Set<PeykUser> getFriendsList() {
		return friendsList;
	}

	public void setFriendsList(Set<PeykUser> friendsList) {
		this.friendsList = friendsList;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PeykUser) {
			PeykUser other = (PeykUser) obj;
			if (other.getHost().equals(host) && other.getPort() == port)
				return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public String toString() {
		return name + " - " + host;
	};

	public String toStringUnique() {
		return host + ":" + port;
	}

	public byte[] serialize() {
		ByteArrayOutputStream out = null;
		ObjectOutputStream os = null;

		try {
			out = new ByteArrayOutputStream();
			os = new ObjectOutputStream(out);
			os.writeObject(this);

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

	public static PeykUser deserialize(byte[] data) {
		ByteArrayInputStream in = null;
		ObjectInputStream is = null;
		PeykUser user = null;

		try {
			in = new ByteArrayInputStream(data);
			is = new ObjectInputStream(in);

			Object obj;
			obj = is.readObject();

			if (obj instanceof PeykUser)
				user = (PeykUser) obj;

		} catch (IOException e) {
			if (e instanceof EOFException) {
				log.error("Deserialize EOFException");
			} else {
				e.printStackTrace();
			}

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

		return user;
	}

	private static String getMyAddress() {
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
