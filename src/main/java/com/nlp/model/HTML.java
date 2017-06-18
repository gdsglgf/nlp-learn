package com.nlp.model;

import java.util.List;

import com.nlp.util.JSONUtils;

public class HTML {
	private int htmlId;
	private String docno;
	private String title;
	private WebURL url;
	private List<WebLink> links;
	public HTML() {
	}
	public HTML(int htmlId) {
		this.htmlId = htmlId;
	}
	public HTML(String docno, String title, WebURL url) {
		this.docno = docno;
		this.title = title;
		this.url = url;
	}
	public HTML(int htmlId, String docno, String title, WebURL url) {
		this.htmlId = htmlId;
		this.docno = docno;
		this.title = title;
		this.url = url;
	}
	public int getHtmlId() {
		return htmlId;
	}
	public void setHtmlId(int htmlId) {
		this.htmlId = htmlId;
	}
	public String getDocno() {
		return docno;
	}
	public void setDocno(String docno) {
		this.docno = docno;
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
	public List<WebLink> getLinks() {
		return links;
	}
	public void setLinks(List<WebLink> links) {
		this.links = links;
	}
	public String toString() {
		return JSONUtils.toJSONString(this);
	}
}
