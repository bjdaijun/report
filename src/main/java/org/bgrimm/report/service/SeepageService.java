package org.bgrimm.report.service;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.dao.BeachFreeHightRepository;
import org.bgrimm.report.dao.SeepageRepository;
import org.bgrimm.report.domain.BeachFreeHeight;
import org.bgrimm.report.domain.Seepage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class SeepageService {
	@Autowired
	private SeepageRepository dao;

	public Seepage findLatestByPosition(Integer position) {
		Page<Seepage> paged = dao.findByMonitoringPosition(position,
				new PageRequest(0, 1, Direction.DESC, "dateTime"));
		Seepage s = paged.getContent().get(0);
		return s;
	}
	
	public List<Seepage> findByPositionAndDateTimeBewteen(Integer position,Date startTime,
			Date endTime) {
		
		
		return dao.findByMonitoringPositionAndDateTimeBetween(position,startTime,
				endTime);
	}

}
