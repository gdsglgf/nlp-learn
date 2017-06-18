package com.nlp.dto;

public class EntityDTO {
	private String type;
	private String name;
	public EntityDTO() {
	}
	public EntityDTO(String type, String name) {
		this.type = type;
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof EntityDTO) {
			EntityDTO dto = (EntityDTO) o;
			return this.name.equals(dto.name);
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return name.hashCode();
	}
}
