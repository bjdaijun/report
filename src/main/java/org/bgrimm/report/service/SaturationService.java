package org.bgrimm.report.service;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.dao.SaturationRepository;
import org.bgrimm.report.domain.Saturation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class SaturationService {
	@Autowired
	private SaturationRepository dao;

	public Saturation findLatestByPosition(Integer position) {
		Page<Saturation> paged = dao.findByMonitoringPosition(position,
				new PageRequest(0, 1, Direction.DESC, "dateTime"));
		Saturation s = paged.getContent().get(0);

		// Saturation
		// s=dao.findTop1ByMonitoringPositionOrderByDateTimeDesc(position);
		return s;
	}

	public List<Saturation> findByPositionAndDateTimeBewteen(Integer position,Date startTime,
			Date endTime) {
		
		
		return dao.findByMonitoringPositionAndDateTimeBetween(position,startTime,
				endTime);
	}

}
