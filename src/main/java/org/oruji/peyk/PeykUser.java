package org.oruji.peyk;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public final class PeykUser {
	private final String host;
	private final int port;

	@SuppressWarnings("unused")
	private PeykUser() {
		this.host = null;
		this.port = 0;
	}

	public PeykUser(int port) {
		this.port = port;
		this.host = "127.0.0.1";
	}

	public PeykUser(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public boolean isOnline() {
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(host, port), 6);
			socket.close();
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
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
		return host + ":" + port;
	}
}
