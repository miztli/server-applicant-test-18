package com.mytaxi.service.car;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;

import java.util.List;

public interface CarService {
    //FIND
    List<CarDO> find ();
    CarDO find (Long id) throws EntityNotFoundException;
    CarDO findByDriverId(long driverId) throws EntityNotFoundException;

    //CREATE
    CarDO create (CarDO carDO) throws ConstraintsViolationException;

    //UPDATE

    //DELETE
    void delete (Long id) throws EntityNotFoundException;
}
