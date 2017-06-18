package com.nlp.dto;

import java.io.Serializable;
import java.util.List;

import com.nlp.util.JSONUtils;

public class WebDTO implements Serializable {
	private static final long serialVersionUID = -6405900166113656243L;
	private String docno;
	private String url;
	private String title;
	private String text;
	private List<LinkDTO> links;
	private boolean hasText;
	public WebDTO() {
	}
	public WebDTO(String docno, String url, String title, String text, List<LinkDTO> links, boolean hasText) {
		this.docno = docno;
		this.url = url;
		this.title = title;
		this.text = text;
		this.links = links;
		this.hasText = hasText;
	}
	public String getDocno() {
		return docno;
	}
	public void setDocno(String docno) {
		this.docno = docno;
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
	public List<LinkDTO> getLinks() {
		return links;
	}
	public void setLinks(List<LinkDTO> links) {
		this.links = links;
	}
	public boolean getHasText() {
		return hasText;
	}
	public void setHasText(boolean hasText) {
		this.hasText = hasText;
	}
	public String toString() {
		return JSONUtils.toJSONString(this);
	}
}
