package com.nlp.dto;

public class MutableInteger {
	private int value;
	public MutableInteger() {
	}
	public MutableInteger(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof MutableInteger) {
			MutableInteger other = (MutableInteger) o;
			return this.value == other.value;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return value;
	}
}
