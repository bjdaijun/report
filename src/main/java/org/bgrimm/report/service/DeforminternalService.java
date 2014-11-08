package org.bgrimm.report.service;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.dao.DeformInternalRepository;
import org.bgrimm.report.domain.DeformInternal;
import org.bgrimm.report.domain.Saturation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class DeforminternalService {
	@Autowired
	private DeformInternalRepository dao;

	public DeformInternal findLatestByPosition(Integer position) {
		Page<DeformInternal> paged = dao.findByMonitoringPosition(position,
				new PageRequest(0, 1, Direction.DESC, "dateTime"));
		DeformInternal s = paged.getContent().get(0);

		// Saturation
		// s=dao.findTop1ByMonitoringPositionOrderByDateTimeDesc(position);
		return s;
	}

	public List<DeformInternal> findByPositionAndDateTimeBewteen(Integer position,
			Date startTime, Date endTime) {

		return dao.findByMonitoringPositionAndDateTimeBetween(position,
				startTime, endTime);
	}

}
