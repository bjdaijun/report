package org.bgrimm.report.dao;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.domain.Seepage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SeepageRepository extends CrudRepository<Seepage, Long> {

	List<Seepage> findByMonitoringPositionAndDateTimeBetween(Integer position,
			Date startTime, Date endTime);

	Page<Seepage> findByMonitoringPosition(Integer position,
			Pageable pageRequest);

}
