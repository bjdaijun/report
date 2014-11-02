package org.bgrimm.report.domain;

import java.math.BigDecimal;
import java.util.Date;

public class SaturationDTO {
	private String name;
	private BigDecimal value;
	private Date dateTime;
	private BigDecimal maxValue;
	private Date maxDateTime;
	private BigDecimal minValue;
	private Date minDateTime;
	private BigDecimal avgValue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public BigDecimal getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

	public Date getMaxDateTime() {
		return maxDateTime;
	}

	public void setMaxDateTime(Date maxDateTime) {
		this.maxDateTime = maxDateTime;
	}

	public BigDecimal getMinValue() {
		return minValue;
	}

	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}

	public Date getMinDateTime() {
		return minDateTime;
	}

	public void setMinDateTime(Date minDateTime) {
		this.minDateTime = minDateTime;
	}

	public BigDecimal getAvgValue() {
		return avgValue;
	}

	public void setAvgValue(BigDecimal avgValue) {
		this.avgValue = avgValue;
	}

	@Override
	public String toString() {
		return "SaturationDTO [name=" + name + ", value=" + value
				+ ", dateTime=" + dateTime + ", maxValue=" + maxValue
				+ ", maxDateTime=" + maxDateTime + ", minValue=" + minValue
				+ ", minDateTime=" + minDateTime + ", avgValue=" + avgValue
				+ "]";
	}

}
