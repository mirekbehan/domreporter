package com.domreporter.model;

public class GroupedClick {
	private int elementId;
	private String elementName;
	private int elementIdCount;
	
		
	public GroupedClick() {
		super();
	}

	public GroupedClick(int elementId, String elementName, int elementIdCount) {
		super();
		this.elementId = elementId;
		this.elementName = elementName;
		this.elementIdCount = elementIdCount;
	}
	
	public int getElementId() {
		return elementId;
	}
	public void setElementId(int elementId) {
		this.elementId = elementId;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public int getElementIdCount() {
		return elementIdCount;
	}
	public void setElementIdCount(int elementIdCount) {
		this.elementIdCount = elementIdCount;
	}
}
