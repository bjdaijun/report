package org.bgrimm.report.dao;

import org.bgrimm.report.domain.DeformSurface;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DeformSurfaceRepository extends
		CrudRepository<DeformSurface, Long> {

}
