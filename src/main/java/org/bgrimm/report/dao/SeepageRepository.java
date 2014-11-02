package org.bgrimm.report.dao;

import org.bgrimm.report.domain.Seepage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SeepageRepository extends CrudRepository<Seepage, Long> {

}
