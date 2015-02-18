package org.oruji.peyk;

import java.io.IOException;
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

	@Override
	public String toString() {
		return host + ":" + port;
	}

	public boolean isOnline() {
		try {
			new Socket(host, port).close();
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
