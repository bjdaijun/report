package org.bgrimm.report.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tdm_deform_surface")
public class DeformSurface {

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
	private int monitoringPosition;
	private BigDecimal dN;
	private BigDecimal dE;
	private BigDecimal dH;
	// @Column(precision = 24, scale = 0)
	// private double dDN;
	// @Column(precision = 24, scale = 0)
	// private double dDE;
	// @Column(precision = 24, scale = 0)
	// private double dDH;

	@Transient
	Object obj;

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	// public double getdDN() {
	// return dDN;
	// }
	//
	// public void setdDN(double dDN) {
	// this.dDN = dDN;
	// }
	//
	// public double getdDE() {
	// return dDE;
	// }
	//
	// public void setdDE(double dDE) {
	// this.dDE = dDE;
	// }
	//
	// public double getdDH() {
	// return dDH;
	// }
	//
	// public void setdDH(double dDH) {
	// this.dDH = dDH;
	// }

	public BigDecimal getdN() {
		return dN;
	}

	public void setdN(BigDecimal dN) {
		this.dN = dN;
	}

	public BigDecimal getdE() {
		return dE;
	}

	public void setdE(BigDecimal dE) {
		this.dE = dE;
	}

	public BigDecimal getdH() {
		return dH;
	}

	public void setdH(BigDecimal dH) {
		this.dH = dH;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public int getMonitoringPosition() {
		return monitoringPosition;
	}

	public void setMonitoringPosition(int monitoringPosition) {
		this.monitoringPosition = monitoringPosition;
	}

	@Override
	public String toString() {
		return "BMWY [logtime=" + dateTime + ", stationId="
				+ monitoringPosition + ", dN=" + dN + ", dE=" + dE + ", dH="
				+ dH + "]";
	}

}