package com.mytaxi.service.car;

import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;

import java.util.List;

public interface CarService {
    List<CarDO> find ();
    CarDO find (Long id) throws EntityNotFoundException;
    ManufacturerDO findCarManufacturer (Long id) throws EntityNotFoundException;
    CarDO create (CarDO carDO) throws ConstraintsViolationException;
    void delete (Long id) throws EntityNotFoundException;
}
