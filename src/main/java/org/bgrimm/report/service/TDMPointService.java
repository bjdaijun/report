package org.bgrimm.report.service;

import java.util.List;

import org.bgrimm.report.dao.TDMPointRepository;
import org.bgrimm.report.domain.TDMPoint;
import org.bgrimm.report.domain.TDMType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TDMPointService {

	@Autowired
	private TDMPointRepository dao;

	public List<TDMPoint> findByType(TDMType type) {
		return dao.findByType(type);
	}

}
