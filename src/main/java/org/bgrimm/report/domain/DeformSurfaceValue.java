package org.bgrimm.report.domain;

import java.math.BigDecimal;
import java.util.Date;

public class DeformSurfaceValue {

	private BigDecimal dn;
	private BigDecimal de;
	private BigDecimal dh;
	private Date dateTime;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public BigDecimal getDn() {
		return dn;
	}
	public void setDn(BigDecimal dn) {
		this.dn = dn;
	}
	public BigDecimal getDe() {
		return de;
	}
	public void setDe(BigDecimal de) {
		this.de = de;
	}
	public BigDecimal getDh() {
		return dh;
	}
	public void setDh(BigDecimal dh) {
		this.dh = dh;
	}
	
}
