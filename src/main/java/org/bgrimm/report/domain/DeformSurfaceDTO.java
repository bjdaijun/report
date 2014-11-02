package org.bgrimm.report.domain;

import java.math.BigDecimal;
import java.sql.Date;

public class DeformSurfaceDTO {
	private String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private BigDecimal dN;
	private BigDecimal dE;
	private BigDecimal dH;
	private Date dateTime;

	private BigDecimal maxDH;
	private BigDecimal maxDE;
	private BigDecimal maxDN;
	private Date maxDateTime;

	private BigDecimal avgDH;
	private BigDecimal avgDE;
	private BigDecimal avgDN;
	private Date avgDateTime;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public BigDecimal getMaxDH() {
		return maxDH;
	}

	public void setMaxDH(BigDecimal maxDH) {
		this.maxDH = maxDH;
	}

	public BigDecimal getMaxDE() {
		return maxDE;
	}

	public void setMaxDE(BigDecimal maxDE) {
		this.maxDE = maxDE;
	}

	public BigDecimal getMaxDN() {
		return maxDN;
	}

	public void setMaxDN(BigDecimal maxDN) {
		this.maxDN = maxDN;
	}

	public Date getMaxDateTime() {
		return maxDateTime;
	}

	public void setMaxDateTime(Date maxDateTime) {
		this.maxDateTime = maxDateTime;
	}

	public BigDecimal getAvgDH() {
		return avgDH;
	}

	public void setAvgDH(BigDecimal avgDH) {
		this.avgDH = avgDH;
	}

	public BigDecimal getAvgDE() {
		return avgDE;
	}

	public void setAvgDE(BigDecimal avgDE) {
		this.avgDE = avgDE;
	}

	public BigDecimal getAvgDN() {
		return avgDN;
	}

	public void setAvgDN(BigDecimal avgDN) {
		this.avgDN = avgDN;
	}

	public Date getAvgDateTime() {
		return avgDateTime;
	}

	public void setAvgDateTime(Date avgDateTime) {
		this.avgDateTime = avgDateTime;
	}

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

}
