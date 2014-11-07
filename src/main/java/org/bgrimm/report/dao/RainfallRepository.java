package org.bgrimm.report.dao;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.domain.Rainfall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RainfallRepository extends CrudRepository<Rainfall, Long> {

	Page<Rainfall> findByMonitoringPosition(Integer position,
			Pageable pageRequest);

	List<Rainfall> findByMonitoringPositionAndDateTimeBetweenOrderByDateTimeAsc(Integer position,
			Date startTime, Date endTime);

}
