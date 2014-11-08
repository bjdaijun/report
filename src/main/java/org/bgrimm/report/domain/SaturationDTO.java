package org.bgrimm.report.domain;

import java.math.BigDecimal;
import java.util.Date;

public class SaturationDTO {
	private String name="";
	private String value="";
	private String dateTime="";
	private String maxValue="";
	private String maxDateTime="";
	private String minValue="";
	private String minDateTime="";
	private String avgValue="";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getMinDateTime() {
		return minDateTime;
	}

	public void setMinDateTime(String minDateTime) {
		this.minDateTime = minDateTime;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getAvgValue() {
		return avgValue;
	}

	public void setAvgValue(String avgValue) {
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
