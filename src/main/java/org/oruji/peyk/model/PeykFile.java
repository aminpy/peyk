package org.oruji.peyk.model;

import java.io.Serializable;

public class PeykFile implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private transient String path;
	private long size;
	private byte[] content;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	public String getAbsolutePath() {
		return this.path + "/" + this.name;
	}
}
