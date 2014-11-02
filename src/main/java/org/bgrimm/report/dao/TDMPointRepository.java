package org.bgrimm.report.dao;

import java.util.List;

import org.bgrimm.report.domain.TDMPoint;
import org.bgrimm.report.domain.TDMType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TDMPointRepository extends CrudRepository<TDMPoint, Long> {

	List<TDMPoint> findByType(TDMType type);
}
