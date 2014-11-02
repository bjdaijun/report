package org.bgrimm.report.dao;

import org.bgrimm.report.domain.DeformInternal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DeformInternalRepository extends
		CrudRepository<DeformInternal, Long> {

}
