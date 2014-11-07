package org.bgrimm.report.service;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.dao.RainfallRepository;
import org.bgrimm.report.domain.DeformSurface;
import org.bgrimm.report.domain.Rainfall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class RainfallService {
	@Autowired
	private RainfallRepository dao;

	public Rainfall findLatestByPosition(Integer position) {
		Page<Rainfall> paged = dao.findByMonitoringPosition(position,
				new PageRequest(0, 1, Direction.DESC, "dateTime"));
		Rainfall s = paged.getContent().get(0);
		return s;
	}

	public List<Rainfall> findByPositionAndDateTimeBewteen(Integer position,
			Date startTime, Date endTime) {

		return dao.findByMonitoringPositionAndDateTimeBetweenOrderByDateTimeAsc(position,
				startTime, endTime);
	}
}
