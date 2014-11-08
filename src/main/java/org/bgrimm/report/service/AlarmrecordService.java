package org.bgrimm.report.service;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.dao.AlarmRecordRepository;
import org.bgrimm.report.domain.AlarmRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlarmrecordService {

	@Autowired
	private AlarmRecordRepository dao;

	public List<AlarmRecord> findAlarmRecordByTimeBetween(Date startTime,
			Date endTime) {
		return dao.findByEventTimeBetween(startTime, endTime);
	}

}
