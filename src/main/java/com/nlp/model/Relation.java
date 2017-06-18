package com.nlp.model;

import com.nlp.util.JSONUtils;

public class Relation {
	private int relaId;
	private Entity subject;
	private Entity object;
	private TypeInfo type;
	public Relation() {
	}
	public Relation(Entity subject, Entity object, TypeInfo type) {
		this.subject = subject;
		this.object = object;
		this.type = type;
	}
	public int getRelaId() {
		return relaId;
	}
	public void setRelaId(int relaId) {
		this.relaId = relaId;
	}
	public Entity getSubject() {
		return subject;
	}
	public void setSubject(Entity subject) {
		this.subject = subject;
	}
	public Entity getObject() {
		return object;
	}
	public void setObject(Entity object) {
		this.object = object;
	}
	public TypeInfo getType() {
		return type;
	}
	public void setType(TypeInfo type) {
		this.type = type;
	}
	public String toString() {
		return JSONUtils.toJSONString(this);
	}
}
