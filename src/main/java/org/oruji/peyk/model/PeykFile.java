package org.oruji.peyk.model;

import java.io.Serializable;

public class PeykFile implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private long size;
	private byte[] content;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
}
