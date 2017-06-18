package com.nlp.model;

import com.nlp.util.JSONUtils;

public class RelationMention {
	private int uid;
	private Relation relation;
	private HTML html;
	private int count;
	public RelationMention() {
	}
	public RelationMention(Relation relation, HTML html, int count) {
		this.relation = relation;
		this.html = html;
		this.count = count;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public Relation getRelation() {
		return relation;
	}
	public void setRelation(Relation relation) {
		this.relation = relation;
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
