package org.bgrimm.report.dao;

import org.bgrimm.report.domain.BeachFreeHeight;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BeachFreeHightRepository extends
		CrudRepository<BeachFreeHeight, Long> {

}
