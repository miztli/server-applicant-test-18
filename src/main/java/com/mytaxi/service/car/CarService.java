package com.mytaxi.service.car;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.BusinessRuleException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;

import java.util.List;

/**
 * Provides the external-facing method interface
 * for working with @{@link CarDO} entities.
 */
public interface CarService {
    //FIND
    List<CarDO> find () throws EntityNotFoundException;
    CarDO find (Long id) throws EntityNotFoundException;
    CarDO findByDriverId(long driverId) throws EntityNotFoundException;

    //CREATE
    CarDO create (CarDO carDO) throws ConstraintsViolationException;

    //UPDATE

    //DELETE
    void delete (Long id) throws EntityNotFoundException, BusinessRuleException;
}
