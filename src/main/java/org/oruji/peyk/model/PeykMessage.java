package org.oruji.peyk.model;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
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
		OutputStream outputStream = null;
		ObjectOutputStream objectOutputStream = null;

		try {
			client = new Socket(this.getReceiver().getHost(), this
					.getReceiver().getPort());

			outputStream = client.getOutputStream();
			objectOutputStream = new ObjectOutputStream(outputStream);

			objectOutputStream.writeObject(this);

		} catch (UnknownHostException e1) {
			e1.printStackTrace();

		} catch (IOException e1) {
			e1.printStackTrace();

		} finally {
			try {
				if (client != null)
					client.close();

				if (outputStream != null)
					outputStream.close();

				if (objectOutputStream != null)
					objectOutputStream.close();

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void sendFile() {
		Socket client = null;
		OutputStream outputStream = null;
		ObjectOutputStream objectOutputStream = null;

		FileInputStream fileInputStream = null;
		DataInputStream dataInputStream = null;

		try {
			client = new Socket(this.getReceiver().getHost(), this
					.getReceiver().getPort());

			outputStream = client.getOutputStream();
			objectOutputStream = new ObjectOutputStream(outputStream);

			File file = new File(this.getAttachedFile().getPath() + "/"
					+ this.getAttachedFile().getName());

			if (file.isFile()) {
				fileInputStream = new FileInputStream(file);
				dataInputStream = new DataInputStream(fileInputStream);

				long len = (int) file.length();
				byte[] fileBytes = new byte[(int) len];
				int read = 0;
				int numRead = 0;

				while (read < fileBytes.length
						&& (numRead = dataInputStream.read(fileBytes, read,
								fileBytes.length - read)) >= 0) {
					read = read + numRead;
				}

				attachedFile.setSize(len);
				attachedFile.setContent(fileBytes);

			} else {
				log.error("file does not exist !");
			}

			objectOutputStream.writeObject(this);

		} catch (UnknownHostException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if (client != null)
					client.close();

				if (outputStream != null)
					outputStream.close();

				if (objectOutputStream != null)
					objectOutputStream.close();

				if (dataInputStream != null)
					dataInputStream.close();

				if (fileInputStream != null)
					fileInputStream.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
