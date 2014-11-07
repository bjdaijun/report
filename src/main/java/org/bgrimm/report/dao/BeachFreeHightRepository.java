package org.bgrimm.report.dao;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.domain.BeachFreeHeight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BeachFreeHightRepository extends
		CrudRepository<BeachFreeHeight, Long> {

	Page<BeachFreeHeight> findByMonitoringPosition(Integer position,
			Pageable pageRequest);

	List<BeachFreeHeight> findByMonitoringPositionAndDateTimeBetween(
			Integer position, Date startTime, Date endTime);

}
