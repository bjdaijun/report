package org.bgrimm.report.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tdm_monitor_point")
public class TDMPoint {
	@Id
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TDMType getType() {
		return type;
	}

	public void setType(TDMType type) {
		this.type = type;
	}

	@ManyToOne
	@JoinColumn(name = "type_id")
	private TDMType type;

}
