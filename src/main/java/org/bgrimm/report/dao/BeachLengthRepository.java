package org.bgrimm.report.dao;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.domain.BeachLength;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BeachLengthRepository extends
		CrudRepository<BeachLength, Long> {

	Page<BeachLength> findByMonitoringPosition(Integer position,
			Pageable pageRequest);

	List<BeachLength> findByMonitoringPositionAndDateTimeBetween(
			Integer position, Date startTime, Date endTime);

}
