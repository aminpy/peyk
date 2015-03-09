package org.oruji.peyk.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
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
	private PeykFile attachedFile;

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

	public PeykFile getAttachedFile() {
		return attachedFile;
	}

	public void setAttachedFile(PeykFile attachedFile) {
		this.attachedFile = attachedFile;
	}

	public void sendMessage() {
		Socket client = null;
		OutputStream outToServer = null;
		ObjectOutputStream out = null;

		try {
			client = new Socket(this.getReceiver().getHost(), this
					.getReceiver().getPort());
			outToServer = client.getOutputStream();
			out = new ObjectOutputStream(outToServer);
			out.writeObject(this);

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

	public String sendFormat() {
		StringBuilder formatedText = new StringBuilder();
		formatedText.append("me");
		formatedText.append(" - ");
		formatedText
				.append(new SimpleDateFormat("HH:mm:ss: ").format(sendDate));
		formatedText.append(text);

		return formatedText.toString();
	}

	public String receiveFormat() {
		StringBuilder formatedText = new StringBuilder();
		formatedText.append(sender.getName());
		formatedText.append(" - ");
		formatedText.append(new SimpleDateFormat("HH:mm:ss")
				.format(receiveDate));
		formatedText.append(": ");
		formatedText.append(text);

		return formatedText.toString();
	}
}
