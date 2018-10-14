package com.mytaxi.dataaccessobject;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Database Access Object for {@link CarDO} entities table.
 * Extends {@link JpaRepository} to have access to some CRUD default methods
 * Extends {@link JpaSpecificationExecutor} to have access to the criteria API
 * for more dynamic queries
 * <p/>
 */
public interface CarRepository extends JpaRepository<CarDO, Long>,
                                       JpaSpecificationExecutor<CarDO> {
//    Optional<CarDO> findByDriverDO(DriverDO driverDO);
    CarDO findByDriverDO(DriverDO driverDO);


    @Query("select c, d from CarDO c left outer join DriverDO d on c.id = d.carDO.id where d.id = ?1")
    CarDO findByDriverId(Long id);
}
