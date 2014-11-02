package org.bgrimm.report.dao;

import org.bgrimm.report.domain.AlarmRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AlarmRecordRepository extends
		CrudRepository<AlarmRecord, Long> {

}
