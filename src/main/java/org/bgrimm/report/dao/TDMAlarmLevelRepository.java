package org.bgrimm.report.dao;

import org.bgrimm.report.domain.TDMAlarmLevel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TDMAlarmLevelRepository extends
		CrudRepository<TDMAlarmLevel, Long> {

}
