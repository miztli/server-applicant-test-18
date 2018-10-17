package com.mytaxi.dataaccessobject.specifications;

import com.mytaxi.domainobject.CarDO;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;

public class CarSpecifications {
    public static Specification<CarDO> findByAssignedDriver(long driverId) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("driverDO").get("id"), driverId);
    }

    public static Specification<CarDO> findByCarId(long carId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            root.fetch("manufacturerDO", JoinType.LEFT);
            return  criteriaBuilder.equal(root.get("id"), carId);
        };
    }
}
