package org.oruji.peyk;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public final class PeykUser implements Serializable {
	private static final long serialVersionUID = 1L;
	private String host;
	private int port;
	private String name;

	@SuppressWarnings("unused")
	private PeykUser() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isOnline() {
		ObjectOutputStream outputStream = null;
		Socket socket = null;
		OutputStream out = null;
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(host, port), 6);
			out = socket.getOutputStream();
			outputStream = new ObjectOutputStream(out);
			outputStream.writeObject(this);
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (socket != null)
					socket.close();
				if (out != null)
					out.close();
				if (outputStream != null)
					outputStream.close();
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
