package com.nlp.model;

import com.nlp.util.JSONUtils;

public class Host {
	private int hostId;
	private String hostname;
	public Host() {
	}
	public Host(String hostname) {
		this.hostname = hostname;
	}
	public Host(int hostId, String hostname) {
		this.hostId = hostId;
		this.hostname = hostname;
	}
	public int getHostId() {
		return hostId;
	}
	public void setHostId(int hostId) {
		this.hostId = hostId;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String toString() {
		return JSONUtils.toJSONString(this);
	}
}
