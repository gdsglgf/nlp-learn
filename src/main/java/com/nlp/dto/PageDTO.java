package com.nlp.dto;

import java.util.Map;

import com.nlp.util.JSONUtils;

public class PageDTO {
	private Integer draw;
	private Integer start;
	private Integer length;
	private Map<String, String> params;
	public PageDTO() {
		draw = 1;
		start = 0;
		length = 10;
	}
	public PageDTO(Integer draw, Integer start, Integer length, Map<String, String> params) {
		this.draw = draw;
		this.start = start;
		this.length = length;
		this.params = params;
	}
	public Integer getDraw() {
		return draw;
	}
	public void setDraw(Integer draw) {
		this.draw = draw;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public String toString() {
		return JSONUtils.toJSONString(this);
	}
}
