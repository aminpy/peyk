package org.oruji.peyk;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
		Socket socket = null;
		OutputStream outToServer = null;
		DataOutputStream out = null;
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(host, port), 6);
			outToServer = socket.getOutputStream();
			out = new DataOutputStream(outToServer);
			out.writeUTF("[[[[ping]]]]");
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (socket != null)
					socket.close();
				if (outToServer != null)
					outToServer.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
