package org.bgrimm.report.dao;

import org.bgrimm.report.domain.Rainfall;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RainfallRepository extends CrudRepository<Rainfall, Long> {

}
