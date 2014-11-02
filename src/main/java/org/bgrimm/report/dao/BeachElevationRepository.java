package org.bgrimm.report.dao;

import org.bgrimm.report.domain.BeachElevation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BeachElevationRepository extends
		CrudRepository<BeachElevation, Long> {

}
