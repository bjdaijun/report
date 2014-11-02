package org.bgrimm.report.dao;

import org.bgrimm.report.domain.BeachLength;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BeachLengthRepository extends
		CrudRepository<BeachLength, Long> {

}
