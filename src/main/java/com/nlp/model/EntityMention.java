package com.nlp.model;

import com.nlp.util.JSONUtils;

public class EntityMention {
	private int uid;
	private Entity entity;
	private HTML html;
	private int count;
	public EntityMention() {
	}
	public EntityMention(Entity entity, HTML html, int count) {
		this.entity = entity;
		this.html = html;
		this.count = count;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	public HTML getHtml() {
		return html;
	}
	public void setHtml(HTML html) {
		this.html = html;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String toString() {
		return JSONUtils.toJSONString(this);
	}
}
