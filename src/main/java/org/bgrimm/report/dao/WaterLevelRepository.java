package org.bgrimm.report.dao;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.domain.WaterLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface WaterLevelRepository extends CrudRepository<WaterLevel, Long> {

	Page<WaterLevel> findByMonitoringPosition(Integer position,
			Pageable pageRequest);

	List<WaterLevel> findByMonitoringPositionAndDateTimeBetween(
			Integer position, Date startTime, Date endTime);

}
