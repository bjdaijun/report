package org.bgrimm.report.dao;

import org.bgrimm.report.domain.WaterLevel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface WaterLevelRepository extends CrudRepository<WaterLevel, Long> {

}
