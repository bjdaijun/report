package org.bgrimm.report.dao;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.domain.AlarmRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRecordRepository extends
		CrudRepository<AlarmRecord, Long> {

	List<AlarmRecord> findByEventTimeBetween(Date startTime, Date endTime);



}
