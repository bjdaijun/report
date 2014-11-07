package org.bgrimm.report.domain;

import java.math.BigDecimal;
import java.util.Date;

public class SaturationDTO {
	private String name;
	private BigDecimal value;
	private String dateTime;
	private BigDecimal maxValue;
	private String maxDateTime;
	private BigDecimal minValue;
	private String minDateTime;
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


	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public BigDecimal getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

	public String getMaxDateTime() {
		return maxDateTime;
	}

	public void setMaxDateTime(String maxDateTime) {
		this.maxDateTime = maxDateTime;
	}

	public BigDecimal getMinValue() {
		return minValue;
	}

	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}

	public String getMinDateTime() {
		return minDateTime;
	}

	public void setMinDateTime(String minDateTime) {
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
