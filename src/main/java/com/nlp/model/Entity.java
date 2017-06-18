package com.nlp.model;

import com.nlp.util.JSONUtils;

public class Entity {
	private int entityId;
	private TypeInfo type;
	private String name;
	public Entity() {
	}
	public Entity(String name) {
		this.name = name;
	}
	public Entity(TypeInfo type, String name) {
		this.type = type;
		this.name = name;
	}
	public int getEntityId() {
		return entityId;
	}
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}
	public TypeInfo getType() {
		return type;
	}
	public void setType(TypeInfo type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String toString() {
		return JSONUtils.toJSONString(this);
	}
}
