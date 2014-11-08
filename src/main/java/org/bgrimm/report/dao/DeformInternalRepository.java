package org.bgrimm.report.dao;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.domain.DeformInternal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DeformInternalRepository extends
		CrudRepository<DeformInternal, Long> {

	Page<DeformInternal> findByMonitoringPosition(Integer position,
			Pageable pageRequest);

	List<DeformInternal> findByMonitoringPositionAndDateTimeBetween(
			Integer position, Date startTime, Date endTime);

}
