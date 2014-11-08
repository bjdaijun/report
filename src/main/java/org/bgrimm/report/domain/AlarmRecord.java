package org.bgrimm.report.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "tdm_alarm_record")
public class AlarmRecord {
	@Id
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "monitor_type_id")
	private TDMType type;
	@ManyToOne
	@JoinColumn(name = "monitor_point_id")
	private TDMPoint point;

	public TDMType getType() {
		return type;
	}

	public void setType(TDMType type) {
		this.type = type;
	}

	public TDMPoint getPoint() {
		return point;
	}

	public void setPoint(TDMPoint point) {
		this.point = point;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BigDecimal getAlarmValue() {
		return alarmValue;
	}

	public void setAlarmValue(BigDecimal alarmValue) {
		this.alarmValue = alarmValue;
	}

	@Column(name = "alarm_time")
	private Date eventTime;
	@Column(name = "alarm_content")
	private String content;
	@Column(name = "alarm_value")
	private BigDecimal alarmValue;
	@Column
	@org.hibernate.annotations.Type(type = "yes_no")
	private boolean closed;
	@Column(name = "closed_time")
	private Date closedTime;
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "alarm_level_id")
	private TDMAlarmLevel level;

	public TDMAlarmLevel getLevel() {
		return level;
	}

	public void setLevel(TDMAlarmLevel level) {
		this.level = level;
	}

	public boolean getClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public Date getClosedTime() {
		return closedTime;
	}

	public void setClosedTime(Date closedTime) {
		this.closedTime = closedTime;
	}

	@Transient
	private long monitoringPointId;

	public long getMonitoringPointId() {
		return monitoringPointId;
	}

	public void setMonitoringPointId(long monitoringPointId) {
		this.monitoringPointId = monitoringPointId;
	}

}
