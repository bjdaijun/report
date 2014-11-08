package org.bgrimm.report.domain;

import java.math.BigDecimal;

public class RainfallDTO {

	@Override
	public String toString() {
		return "RainfallDTO [name=" + name + ", dateTime=" + dateTime
				+ ", value=" + value + ", maxValue=" + maxValue
				+ ", maxDateTime=" + maxDateTime + ", totalValue=" + totalValue
				+ ", totalTime=" + totalTime + "]";
	}

	private String name="";
	private String dateTime="";

	private String value="";

	private String maxValue="";
	private String maxDateTime="";

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

	public String getTotalTime() {
		return totalTime;
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

	public String getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	private String totalValue="";
	private String totalTime="";
}
