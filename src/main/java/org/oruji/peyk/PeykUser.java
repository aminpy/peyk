package org.oruji.peyk;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public final class PeykUser implements Serializable {
	private static final long serialVersionUID = 1L;
	private String host;
	private final int port = 8180;
	private String name;
	private static PeykUser sourceUser = null;
	private Set<PeykUser> friendsList = new CopyOnWriteArraySet<PeykUser>();

	public static PeykUser getSourceUser() {
		if (sourceUser != null)
			return sourceUser;

		sourceUser = new PeykUser(OnlineBroadCast.getMyAddress());
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
}
