package org.bgrimm.report.dao;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.domain.BeachCrestHeight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeachCrestHeightRepository extends
		CrudRepository<BeachCrestHeight, Long> {

	Page<BeachCrestHeight> findByMonitoringPosition(Integer position,
			Pageable pageRequest);

	List<BeachCrestHeight> findByMonitoringPositionAndDateTimeBetween(
			Integer position, Date startTime, Date endTime);

}
