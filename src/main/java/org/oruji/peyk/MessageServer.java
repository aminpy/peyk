package org.oruji.peyk;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import org.apache.log4j.Logger;
import org.oruji.peyk.model.PeykMessage;
import org.oruji.peyk.model.PeykUser;

public class MessageServer implements Runnable {
	Logger log = Logger.getLogger(MessageServer.class.getName());

	public void run() {
		while (true) {
			PeykMessage message = receiveMessage(PeykUser.getSourceUser()
					.getPort());

			if (message == null)
				continue;

			ChatFrame chatFrame = ChatFrame.getChatFrame(message.getSender());

			chatFrame.appendText(message.receiveFormat());
		}
	}

	public void start() {
		new Thread(this, "Server Thread").start();
	}

	private PeykMessage receiveMessage(int port) {
		ServerSocket serverSocket = null;
		Socket server = null;
		ObjectInputStream in = null;
		PeykMessage message = null;

		try {
			serverSocket = new ServerSocket(port);
			log.info("before server accept <<<<<<<<<<<<<<<");
			server = serverSocket.accept();
			log.info("after  server accept >>>>>>>>>>>>>>>");

			InputStream inputStream = server.getInputStream();

			in = new ObjectInputStream(inputStream);
			Object inputObj = in.readObject();

			if (inputObj instanceof PeykMessage) {
				message = (PeykMessage) inputObj;
				message.setReceiveDate(new Date());
			}

		} catch (IOException e) {
			if (e instanceof EOFException) {
				log.error("EOF Exception occured !");
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		} finally {
			try {
				if (serverSocket != null)
					serverSocket.close();

				if (server != null)
					server.close();

				if (in != null)
					in.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return message;
	}
}
