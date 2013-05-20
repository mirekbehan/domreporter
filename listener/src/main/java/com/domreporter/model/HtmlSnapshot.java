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
@Table(name="document_dom")
@XmlRootElement
public class HtmlSnapshot {
	private int id;
	private String dom;
	private Date snapshotCreationTime;
	private String pageSessionId;
	private String url;
		

	public HtmlSnapshot() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="html_dom")
	public String getDom() {
		return dom;
	}

	public void setDom(String dom) {
		this.dom = dom;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="document_timestamp") 
	public Date getSnapshotCreationTime() {
		return snapshotCreationTime;
	}

	public void setSnapshotCreationTime(Date snapshotCreationTime) {
		this.snapshotCreationTime = snapshotCreationTime;
	}
	@Column(name="page_session_id", length=36)
	public String getPageSessionId() {
		return pageSessionId;
	}

	public void setPageSessionId(String pageSessionId) {
		this.pageSessionId = pageSessionId;
	}
	
	@Column(name="url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
