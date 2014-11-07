package org.bgrimm.report.service;

import java.util.Date;
import java.util.List;

import org.bgrimm.report.dao.DeformSurfaceRepository;
import org.bgrimm.report.domain.DeformSurface;
import org.bgrimm.report.domain.Saturation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class DeformsurfaceService {

	@Autowired
	private DeformSurfaceRepository dao;

	public DeformSurface findLatestByPosition(Integer position) {
		Page<DeformSurface> paged = dao.findByMonitoringPosition(position,
				new PageRequest(0, 1, Direction.DESC, "dateTime"));
		DeformSurface s = paged.getContent().get(0);
		return s;
	}
	
	public List<DeformSurface> findByPositionAndDateTimeBewteen(Integer position,Date startTime,
			Date endTime) {
		
		
		return dao.findByMonitoringPositionAndDateTimeBetween(position,startTime,
				endTime);
	}
}
