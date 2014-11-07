package org.bgrimm.report.dao;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.domain.DeformSurface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeformSurfaceRepository extends
		CrudRepository<DeformSurface, Long> {

	Page<DeformSurface> findByMonitoringPosition(Integer position, Pageable page);

	List<DeformSurface> findByMonitoringPositionAndDateTimeBetween(
			Integer position, Date startTime, Date endTime);
}
