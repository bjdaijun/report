package org.bgrimm.report.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tdm_rainfall")
public class Rainfall {
	@Id
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "date_Time")
	private Date dateTime;

	@Column(name = "monitoring_position")
	private Integer monitoringPosition;

	@Column(name = "value")
	private BigDecimal value;

	private BigDecimal acc;

	public BigDecimal getAcc() {
		return acc;
	}

	public void setAcc(BigDecimal acc) {
		this.acc = acc;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Integer getMonitoringPosition() {
		return monitoringPosition;
	}

	public void setMonitoringPosition(Integer monitoringPosition) {
		this.monitoringPosition = monitoringPosition;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}
