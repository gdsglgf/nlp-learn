package com.nlp.dto;

import java.io.Serializable;

import com.nlp.util.JSONUtils;

public class LinkDTO implements Serializable {
	private static final long serialVersionUID = -4863112079634222308L;
	private String href;
	private String text;
	public LinkDTO() {
	}
	public LinkDTO(String href, String text) {
		this.href = href;
		this.text = text;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof LinkDTO) {
			LinkDTO link = (LinkDTO)o;
			return this.href.equals(link.href);
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return href.hashCode();
	}
}
