package com.nlp.model;

import com.nlp.util.JSONUtils;

public class WebURL {
	private int urlId;
	private String url;
	private Host host;
	public WebURL() {
	}
	public WebURL(String url, Host host) {
		this.url = url;
		this.host = host;
	}
	public WebURL(int urlId, String url, Host host) {
		this.urlId = urlId;
		this.url = url;
		this.host = host;
	}
	public int getUrlId() {
		return urlId;
	}
	public void setUrlId(int urlId) {
		this.urlId = urlId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Host getHost() {
		return host;
	}
	public void setHost(Host host) {
		this.host = host;
	}
	public String toString() {
		return JSONUtils.toJSONString(this);
	}
}
