package org.oruji.peyk;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.log4j.Logger;

public class PeykMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Logger log = Logger.getLogger(PeykMessage.class.getName());

	private String text;
	private Date receiveDate;
	private Date sendDate;
	private PeykUser sender;
	private PeykUser receiver;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public PeykUser getSender() {
		return sender;
	}

	public void setSender(PeykUser sender) {
		this.sender = sender;
	}

	public PeykUser getReceiver() {
		return receiver;
	}

	public void setReceiver(PeykUser receiver) {
		this.receiver = receiver;
	}

	public static void sendMessage(PeykMessage message) {
		Socket client = null;
		OutputStream outToServer = null;
		ObjectOutputStream out = null;

		try {
			client = new Socket(message.getReceiver().getHost(), message
					.getReceiver().getPort());
			outToServer = client.getOutputStream();
			out = new ObjectOutputStream(outToServer);
			out.writeObject(message);

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

	public static PeykMessage receiveMessage(int port) {
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

			// String host = server.toString().split("=")[1].split(",port")[0]
			// .substring(1);

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sendDate == null) ? 0 : sendDate.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeykMessage other = (PeykMessage) obj;
		if (sendDate == null) {
			if (other.sendDate != null)
				return false;
		} else if (!sendDate.equals(other.sendDate))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
}
