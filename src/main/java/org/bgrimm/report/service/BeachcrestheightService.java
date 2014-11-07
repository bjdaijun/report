package org.bgrimm.report.service;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.dao.BeachCrestHeightRepository;
import org.bgrimm.report.domain.BeachCrestHeight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class BeachcrestheightService {
	@Autowired
	private BeachCrestHeightRepository dao;

	public BeachCrestHeight findLatestByPosition(Integer position) {
		Page<BeachCrestHeight> paged = dao.findByMonitoringPosition(position,
				new PageRequest(0, 1, Direction.DESC, "dateTime"));
		BeachCrestHeight s = paged.getContent().get(0);
		return s;
	}
	
	public List<BeachCrestHeight> findByPositionAndDateTimeBewteen(Integer position,Date startTime,
			Date endTime) {
		
		
		return dao.findByMonitoringPositionAndDateTimeBetween(position,startTime,
				endTime);
	}
}
