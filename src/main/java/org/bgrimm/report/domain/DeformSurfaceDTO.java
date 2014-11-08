package org.bgrimm.report.domain;


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

	private String DN="";
	private String DE="";
	private String DH="";
	private String dateTime="";

	private String maxDH="";
	private String maxDE="";
	private String maxDN="";
	private String maxDateTime="";

	private String minDH="";
	private String minDE="";
	private String minDN="";
	private String minDateTime="";

	public String getMinDateTime() {
		return minDateTime;
	}

	public void setMinDateTime(String minDateTime) {
		this.minDateTime = minDateTime;
	}

	private String avgDH="";
	private String avgDE="";
	private String avgDN="";
	private String avgDateTime="";

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

	public String getDN() {
		return DN;
	}

	public void setDN(String dN) {
		DN = dN;
	}

	public String getDE() {
		return DE;
	}

	public void setDE(String dE) {
		DE = dE;
	}

	public String getDH() {
		return DH;
	}

	public void setDH(String dH) {
		DH = dH;
	}

	public String getMaxDH() {
		return maxDH;
	}

	public void setMaxDH(String maxDH) {
		this.maxDH = maxDH;
	}

	public String getMaxDE() {
		return maxDE;
	}

	public void setMaxDE(String maxDE) {
		this.maxDE = maxDE;
	}

	public String getMaxDN() {
		return maxDN;
	}

	public void setMaxDN(String maxDN) {
		this.maxDN = maxDN;
	}

	public String getMinDH() {
		return minDH;
	}

	public void setMinDH(String minDH) {
		this.minDH = minDH;
	}

	public String getMinDE() {
		return minDE;
	}

	public void setMinDE(String minDE) {
		this.minDE = minDE;
	}

	public String getMinDN() {
		return minDN;
	}

	public void setMinDN(String minDN) {
		this.minDN = minDN;
	}

	public String getAvgDH() {
		return avgDH;
	}

	public void setAvgDH(String avgDH) {
		this.avgDH = avgDH;
	}

	public String getAvgDE() {
		return avgDE;
	}

	public void setAvgDE(String avgDE) {
		this.avgDE = avgDE;
	}

	public String getAvgDN() {
		return avgDN;
	}

	public void setAvgDN(String avgDN) {
		this.avgDN = avgDN;
	}

}
