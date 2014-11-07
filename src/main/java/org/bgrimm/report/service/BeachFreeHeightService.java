package org.bgrimm.report.service;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.dao.BeachCrestHeightRepository;
import org.bgrimm.report.dao.BeachFreeHightRepository;
import org.bgrimm.report.domain.BeachCrestHeight;
import org.bgrimm.report.domain.BeachFreeHeight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class BeachFreeHeightService {
	@Autowired
	private BeachFreeHightRepository dao;

	public BeachFreeHeight findLatestByPosition(Integer position) {
		Page<BeachFreeHeight> paged = dao.findByMonitoringPosition(position,
				new PageRequest(0, 1, Direction.DESC, "dateTime"));
		BeachFreeHeight s = paged.getContent().get(0);
		return s;
	}
	
	public List<BeachFreeHeight> findByPositionAndDateTimeBewteen(Integer position,Date startTime,
			Date endTime) {
		
		
		return dao.findByMonitoringPositionAndDateTimeBetween(position,startTime,
				endTime);
	}
}
