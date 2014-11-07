package org.bgrimm.report.service;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.dao.WaterLevelRepository;
import org.bgrimm.report.domain.DeformSurface;
import org.bgrimm.report.domain.WaterLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class WaterLevelService {

	@Autowired
	private WaterLevelRepository dao;

	public WaterLevel findLatestByPosition(Integer position) {
		Page<WaterLevel> paged = dao.findByMonitoringPosition(position,
				new PageRequest(0, 1, Direction.DESC, "dateTime"));
		WaterLevel s = paged.getContent().get(0);
		return s;
	}
	public List<WaterLevel> findByPositionAndDateTimeBewteen(Integer position,Date startTime,
			Date endTime) {
		
		
		return dao.findByMonitoringPositionAndDateTimeBetween(position,startTime,
				endTime);
	}
}
