package org.oruji.peyk;

import java.util.Vector;
import java.util.concurrent.TimeoutException;

import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingUtil;

public class OnlineUser {
	public static void main(String[] args) {
		System.out.println(getOnlines());
	}

	public static Vector<String> getOnlines() {
		Vector<String> myVec = new Vector<String>();
		for (int i = 2; i < 254; i++) {
			String host = "192.168.1." + i;
			if (isReachable(host)) {
				myVec.add(host);
			}
		}

		return myVec;
	}

	public static boolean isReachable(final String host) {
		try {
			Runnable runn = new Runnable() {
				public void run() {
					IcmpPingRequest request = IcmpPingUtil
							.createIcmpPingRequest();
					request.setHost(host);
					IcmpPingUtil.executePingRequest(request);
				}
			};

			TimeoutController.execute(runn, 30);
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}
}
