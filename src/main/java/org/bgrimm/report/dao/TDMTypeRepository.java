package org.bgrimm.report.dao;

import org.bgrimm.report.domain.TDMType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TDMTypeRepository extends CrudRepository<TDMType, Long> {

	TDMType findByType(String type);
}
