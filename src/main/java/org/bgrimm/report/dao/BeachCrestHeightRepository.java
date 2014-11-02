package org.bgrimm.report.dao;

import org.bgrimm.report.domain.BeachCrestHeight;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BeachCrestHeightRepository extends
		CrudRepository<BeachCrestHeight, Long> {

}
