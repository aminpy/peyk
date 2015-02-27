package org.oruji.peyk;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public class PeykMessage {
	public static Logger log = Logger.getLogger(PeykMessage.class.getName());

	public static void sendMessage(PeykUser destUser) {
		Socket client = null;
		OutputStream outToServer = null;
		ObjectOutputStream out = null;

		try {
			client = new Socket(destUser.getHost(), destUser.getPort());
			outToServer = client.getOutputStream();
			out = new ObjectOutputStream(outToServer);
			PeykUser sourceUser = PeykUser.getSourceUser();
			sourceUser.setMessage(destUser.getMessage());
			out.writeObject(sourceUser);

		} catch (UnknownHostException e1) {
			e1.printStackTrace();

		} catch (IOException e1) {
			e1.printStackTrace();

		} finally {
			try {
				if (client != null)
					client.close();

				if (outToServer != null)
					outToServer.close();

				if (out != null)
					out.close();

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static PeykUser receiveMessage(int port) {
		ServerSocket serverSocket = null;
		Socket server = null;
		ObjectInputStream in = null;
		PeykUser peykUser = null;

		try {
			serverSocket = new ServerSocket(port);
			log.info("before server accept <<<<<<<<<<<<<<<");
			server = serverSocket.accept();
			log.info("after  server accept >>>>>>>>>>>>>>>");

			InputStream inputStream = server.getInputStream();

			in = new ObjectInputStream(inputStream);
			Object inputObj = in.readObject();

			String host = server.toString().split("=")[1].split(",port")[0]
					.substring(1);
			peykUser = new PeykUser(host);
			PeykUser receivedUser = (PeykUser) inputObj;
			peykUser.setName(receivedUser.getName());
			peykUser.setMessage(receivedUser.getMessage());

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

		return peykUser;
	}
}
