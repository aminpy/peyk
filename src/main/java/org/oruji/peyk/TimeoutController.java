package org.oruji.peyk;

import java.util.concurrent.TimeoutException;

public final class TimeoutController {
	public static void execute(Thread task, long timeout)
			throws TimeoutException {
		task.start();
		try {
			task.join(timeout);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		if (task.isAlive()) {
			task.interrupt();
			throw new TimeoutException();
		}
	}

	public static void execute(Runnable task, long timeout)
			throws TimeoutException {
		Thread t = new Thread(task);
		t.setDaemon(true);
		execute(t, timeout);
	}
}