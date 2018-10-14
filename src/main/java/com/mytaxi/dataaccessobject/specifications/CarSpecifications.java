package com.mytaxi.dataaccessobject.specifications;

import com.mytaxi.domainobject.CarDO;
import org.springframework.data.jpa.domain.Specification;

public class CarSpecifications {
    public static Specification<CarDO> findByAssignedDriver(long driverId) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("driverDO").get("id"), driverId);
    }
}
