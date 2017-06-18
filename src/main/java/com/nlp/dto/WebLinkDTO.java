package com.nlp.dto;

import com.nlp.model.WebURL;
import com.nlp.util.JSONUtils;

public class WebLinkDTO {
	private int id;
	private WebURL url;
	private int in;
	private int out;
	private String title;
	public WebLinkDTO() {
	}
	public WebLinkDTO(int id, WebURL url, int in, int out, String title) {
		this.id = id;
		this.url = url;
		this.in = in;
		this.out = out;
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIn() {
		return in;
	}
	public void setIn(int in) {
		this.in = in;
	}
	public int getOut() {
		return out;
	}
	public void setOut(int out) {
		this.out = out;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public WebURL getUrl() {
		return url;
	}
	public void setUrl(WebURL url) {
		this.url = url;
	}
	public String toString() {
		return JSONUtils.toJSONString(this);
	}
}
