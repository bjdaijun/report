package org.bgrimm.report.domain;

import java.math.BigDecimal;
import java.sql.Date;

public class DeformSurfaceDTO {
	private String name;

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "DeformSurfaceDTO [name=" + name + ", DN=" + DN + ", DE=" + DE
				+ ", DH=" + DH + ", dateTime=" + dateTime + ", maxDH=" + maxDH
				+ ", maxDE=" + maxDE + ", maxDN=" + maxDN + ", maxDateTime="
				+ maxDateTime + ", minDH=" + minDH + ", minDE=" + minDE
				+ ", minDN=" + minDN + ", minDateTime=" + minDateTime
				+ ", avgDH=" + avgDH + ", avgDE=" + avgDE + ", avgDN=" + avgDN
				+ ", avgDateTime=" + avgDateTime + "]";
	}

	public void setName(String name) {
		this.name = name;
	}

	private BigDecimal DN;
	private BigDecimal DE;
	private BigDecimal DH;
	private String dateTime;

	private BigDecimal maxDH;
	private BigDecimal maxDE;
	private BigDecimal maxDN;
	private String maxDateTime;

	private BigDecimal minDH;
	private BigDecimal minDE;
	private BigDecimal minDN;
	private String minDateTime;

	public BigDecimal getMinDH() {
		return minDH;
	}

	public void setMinDH(BigDecimal minDH) {
		this.minDH = minDH;
	}

	public BigDecimal getMinDE() {
		return minDE;
	}

	public void setMinDE(BigDecimal minDE) {
		this.minDE = minDE;
	}

	public BigDecimal getMinDN() {
		return minDN;
	}

	public void setMinDN(BigDecimal minDN) {
		this.minDN = minDN;
	}

	public String getMinDateTime() {
		return minDateTime;
	}

	public void setMinDateTime(String minDateTime) {
		this.minDateTime = minDateTime;
	}

	private BigDecimal avgDH;
	private BigDecimal avgDE;
	private BigDecimal avgDN;
	private String avgDateTime;

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

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getMaxDateTime() {
		return maxDateTime;
	}

	public void setMaxDateTime(String maxDateTime) {
		this.maxDateTime = maxDateTime;
	}

	public String getAvgDateTime() {
		return avgDateTime;
	}

	public void setAvgDateTime(String avgDateTime) {
		this.avgDateTime = avgDateTime;
	}

	public BigDecimal getDN() {
		return DN;
	}

	public void setDN(BigDecimal dN) {
		DN = dN;
	}

	public BigDecimal getDE() {
		return DE;
	}

	public void setDE(BigDecimal dE) {
		DE = dE;
	}

	public BigDecimal getDH() {
		return DH;
	}

	public void setDH(BigDecimal dH) {
		DH = dH;
	}

}
