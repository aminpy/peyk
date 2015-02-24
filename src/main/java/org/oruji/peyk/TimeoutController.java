package org.oruji.peyk;

import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

public final class TimeoutController {
	static Logger log = Logger.getLogger(TimeoutController.class.getName());

	public static void execute(Thread task, long timeout)
			throws TimeoutException {
		task.start();
		try {
			task.join(timeout);
		} catch (InterruptedException e) {
			log.error(e.getMessage());
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