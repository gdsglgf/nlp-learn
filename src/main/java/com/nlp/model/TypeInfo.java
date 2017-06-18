package com.nlp.model;

import com.nlp.util.JSONUtils;

public class TypeInfo {
	private int typeId;
	private TypeInfo ptype;
	private String description;
	public TypeInfo() {
		this(-1, new TypeInfo(-1, null, ""), "");
	}
	public TypeInfo(int typeId) {
		this(typeId, new TypeInfo(-1, null, ""), "");
	}
	public TypeInfo(String description) {
		this(-1, new TypeInfo(-1, null, ""), description);
	}
	public TypeInfo(int typeId, TypeInfo ptype, String description) {
		this.typeId = typeId;
		this.ptype = ptype;
		this.description = description;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public TypeInfo getPtype() {
		return ptype;
	}
	public void setPtype(TypeInfo ptype) {
		this.ptype = ptype;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String toString() {
		return JSONUtils.toJSONString(this);
	}
}
