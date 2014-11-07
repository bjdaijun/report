package org.bgrimm.report.service;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.dao.BeachLengthRepository;
import org.bgrimm.report.dao.DeformSurfaceRepository;
import org.bgrimm.report.domain.BeachLength;
import org.bgrimm.report.domain.DeformSurface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class BeachlengthService {

	@Autowired
	private BeachLengthRepository dao;

	public BeachLength findLatestByPosition(Integer position) {
		Page<BeachLength> paged = dao.findByMonitoringPosition(position,
				new PageRequest(0, 1, Direction.DESC, "dateTime"));
		BeachLength s = paged.getContent().get(0);
		return s;
	}
	
	public List<BeachLength> findByPositionAndDateTimeBewteen(Integer position,Date startTime,
			Date endTime) {
		
		
		return dao.findByMonitoringPositionAndDateTimeBetween(position,startTime,
				endTime);
	}
}
