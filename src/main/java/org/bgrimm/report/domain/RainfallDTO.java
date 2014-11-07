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


	private String name;
	private String dateTime;

	private BigDecimal value;

	private BigDecimal maxValue;
	private String maxDateTime;

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

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
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

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}


	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}


	private BigDecimal totalValue;
	private String totalTime;
}
