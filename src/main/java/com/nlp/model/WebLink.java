package com.nlp.model;

import com.nlp.util.JSONUtils;

public class WebLink {
	private int linkId;
	private int urlId;
	private WebURL urlOut;
	private String text;
	public WebLink() {
	}
	public WebLink(int urlId, WebURL urlOut, String text) {
		this.urlId = urlId;
		this.urlOut = urlOut;
		this.text = text;
	}
	public WebLink(int linkId, int urlId, WebURL urlOut, String text) {
		this.linkId = linkId;
		this.urlId = urlId;
		this.urlOut = urlOut;
		this.text = text;
	}
	public int getLinkId() {
		return linkId;
	}
	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}
	public int getUrlId() {
		return urlId;
	}
	public void setUrlId(int urlId) {
		this.urlId = urlId;
	}
	public WebURL getUrlOut() {
		return urlOut;
	}
	public void setUrlOut(WebURL urlOut) {
		this.urlOut = urlOut;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String toString() {
		return JSONUtils.toJSONString(this);
	}
}
