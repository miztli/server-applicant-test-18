package com.mytaxi.dataaccessobject;

import com.mytaxi.domainobject.ManufacturerDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Database Access Object for {@link com.mytaxi.domainobject.ManufacturerDO} entities table.
 * Extends {@link JpaRepository} to have access to some CRUD default methods
 * Extends {@link JpaSpecificationExecutor} to have access to the criteria API
 * for more dynamic queries
 * <p/>
 */
public interface ManufacturerRepository extends JpaRepository<ManufacturerDO, Long>, JpaSpecificationExecutor<ManufacturerDO> {
}
