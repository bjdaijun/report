package org.bgrimm.report.dao;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.domain.Saturation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaturationRepository extends
		PagingAndSortingRepository<Saturation, Long> {
	// @Query("select u from Saturation u where u.id= ?1")
	// Saturation findLatestByPosition(Integer position);

	@Query("select s from Saturation s where s.monitoringPosition= ?1")
	List<Saturation> findByMonitoringPosition(Integer monitoringPosition);

	Page<Saturation> findByMonitoringPosition(Integer position,
			Pageable page);
	
	List<Saturation> findByMonitoringPositionAndDateTimeBetween(Integer position,Date startTime,Date endTime);
}
