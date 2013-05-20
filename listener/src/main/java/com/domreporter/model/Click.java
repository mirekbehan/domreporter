package com.domreporter.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="click")
@XmlRootElement
public class Click {
	private int id;
	private int x;
	private int y;
	private int elementY;
	private int elementX;
	private int elementId;
	private String elementName;
	private String url;
	private Date timeOfClick;
	private String clientSessionId;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="x")
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	@Column(name="y")
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	@Column(name="element_y")
	public int getElementY() {
		return elementY;
	}
	public void setElementY(int elementY) {
		this.elementY = elementY;
	}
	@Column(name="element_x")
	public int getElementX() {
		return elementX;
	}
	public void setElementX(int elementX) {
		this.elementX = elementX;
	}
	@Column(name="element_id")
	public int getElementId() {
		return elementId;
	}
	public void setElementId(int elementId) {
		this.elementId = elementId;
	}
	@Column(name="element_name")
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	@Column(name="url")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="click_timestamp")
	public Date getTimeOfClick() {
		return timeOfClick;
	}
	public void setTimeOfClick(Date timeOfClick) {
		this.timeOfClick = timeOfClick;
	}
	@Column(name="page_session_id")
	public String getClientSessionId() {
		return clientSessionId;
	}
	public void setClientSessionId(String clientSessionId) {
		this.clientSessionId = clientSessionId;
	}
	
	
	
}
