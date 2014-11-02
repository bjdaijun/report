package org.bgrimm.report.service;

import org.bgrimm.report.dao.TDMTypeRepository;
import org.bgrimm.report.domain.TDMType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TDMTypeService {

	@Autowired
	private TDMTypeRepository dao;

	public TDMType findByType(String type) {
		return dao.findByType(type);
	}

}
