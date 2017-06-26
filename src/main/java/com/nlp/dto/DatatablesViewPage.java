package com.nlp.dto;
import java.util.ArrayList;
import java.util.List;

public class DatatablesViewPage<T> {
	private List<T> aaData; // aaData 与datatales 加载的"dataSrc"对应
	private int draw;
	private int recordsTotal;
	private int recordsFiltered;

	public DatatablesViewPage() {
		aaData = new ArrayList<T>();
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public List<T> getAaData() {
		return aaData;
	}

	public void setAaData(List<T> aaData) {
		this.aaData = aaData;
	}

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public int getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
}