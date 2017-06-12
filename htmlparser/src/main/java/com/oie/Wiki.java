package com.oie;

public class Wiki {
	private int id;
	private String url;
	private String title;
	private String text;

	public Wiki() {
	}

	public Wiki(int id, String url, String title, String text) {
		this.id = id;
		this.url = url;
		this.title = title;
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
