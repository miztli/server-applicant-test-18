package com.mytaxi.service.driver;

import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.BusinessRuleException;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;

import java.util.List;
import java.util.Map;

public interface DriverService
{

    //FIND
    DriverDO find(Long driverId) throws EntityNotFoundException;
    List<DriverDO> find(OnlineStatus onlineStatus);
    List<DriverDO> findAll();
    List<DriverDO> search(Map<String, Object> filters);

    //CREATE
    DriverDO create(DriverDO driverDO) throws ConstraintsViolationException;

    //DELETE
    void delete(Long driverId) throws EntityNotFoundException;

    //UPDATE
    void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException;
    void updateOnlineStatus(long driverId, OnlineStatus onlineStatus) throws EntityNotFoundException;
    void updateDriverCar(long driverId, long carId, boolean selected) throws EntityNotFoundException, CarAlreadyInUseException, BusinessRuleException;
    void updateDriverPartially(long driverId, Map<String, Object> fields) throws EntityNotFoundException;

}
