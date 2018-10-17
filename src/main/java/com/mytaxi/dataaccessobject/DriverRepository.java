package com.mytaxi.dataaccessobject;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * Database Access Object for driver table.
 * Change example implementation to extend more default methods, however,
 * this will not have any side effect in actual code.
 * Extends {@link JpaRepository} to have access to some CRUD default methods.
 * Extends {@link JpaSpecificationExecutor} to have access to the criteria API.
 * for more dynamic queries
 * <p/>
 */
public interface DriverRepository extends JpaRepository<DriverDO, Long>,
                                          JpaSpecificationExecutor<DriverDO>
{

    Optional<DriverDO> findByCarDO(CarDO carDO);

    List<DriverDO> findByOnlineStatus(OnlineStatus onlineStatus);
}
