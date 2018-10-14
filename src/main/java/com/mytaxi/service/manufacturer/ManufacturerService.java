package com.mytaxi.service.manufacturer;

import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.exception.EntityNotFoundException;

public interface ManufacturerService {
    ManufacturerDO findCarManufacturer(long carId) throws EntityNotFoundException;;
}
