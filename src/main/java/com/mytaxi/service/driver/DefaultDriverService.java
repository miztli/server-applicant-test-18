package com.mytaxi.service.driver;

import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.dataaccessobject.specifications.DriverSpecifications;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.GeoCoordinate;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.BusinessRuleException;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some driver specific things.
 * <p/>
 */
@Service
public class DefaultDriverService extends AbstractService<DriverDO, Long> implements DriverService
{

    private static final Logger LOG = LoggerFactory.getLogger(DefaultDriverService.class);

    private final DriverRepository driverRepository;
    private final CarRepository carRepository;


    public DefaultDriverService(final DriverRepository driverRepository, final CarRepository carRepository)
    {
        this.driverRepository = driverRepository;
        this.carRepository = carRepository;
    }


    /**
     * Selects a driver by id.
     *
     * @param driverId
     * @return found driver
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    @Transactional(readOnly = true)
    public DriverDO find(Long driverId) throws EntityNotFoundException
    {
        // replace with abstract method
//        return findDriverChecked(driverId);
        return super.findById(driverId);
    }

    /**
     * Creates a new driver.
     *
     * @param driverDO
     * @return
     * @throws ConstraintsViolationException if a driver already exists with the given username, ... .
     */
    @Override
    @Transactional
    public DriverDO create(DriverDO driverDO) throws ConstraintsViolationException
    {
        DriverDO driver;
        try
        {
            driver = driverRepository.save(driverDO);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("ConstraintsViolationException while creating a driver: {}", driverDO, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return driver;
    }


    /**
     * Deletes an existing driver by id.
     *
     * @param driverId
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long driverId) throws EntityNotFoundException
    {
//        DriverDO driverDO = findDriverChecked(driverId); Replaced with abstract
        DriverDO driverDO = super.findById(driverId);
        driverDO.setDeleted(true);
    }


    /**
     * Update the location for a driver.
     *
     * @param driverId
     * @param longitude
     * @param latitude
     * @throws EntityNotFoundException
     */
    @Override
    @Transactional
    public void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException
    {
        DriverDO driverDO = super.findById(driverId);
        driverDO.setCoordinate(new GeoCoordinate(latitude, longitude));
    }

    /**
     * Sets a car to a driver if car hasn't been assigned already.
     * This method is thread-safe at application layer, if several
     * HTTP Threads should try to access at the same time, they will
     * be queued
     * @param driverId
     * @param carId
     * @param selected
     * @throws EntityNotFoundException
     */
    @Override
    @Transactional
    public synchronized void updateDriverCar(long driverId, long carId, boolean selected) throws EntityNotFoundException, CarAlreadyInUseException, BusinessRuleException {
        DriverDO driverDO = super.findById(driverId); //use abstract to access exception

        //Only online drivers can select/deselect a car
        if(driverDO.getOnlineStatus() == OnlineStatus.OFFLINE){
            throw new BusinessRuleException("Driver must be online to select/deselect a car");
        }
        // Via specification executor method
//        Optional<DriverDO> assignedDriver = getJpaSpecificationExecutor().findOne(DriverSpecifications.findByCarId(carId));

        // Via repository method
        Optional<DriverDO> assignedDriver = driverRepository.findByCarDO(new CarDO(carId));

        if(selected) {
            if (assignedDriver.isPresent() && assignedDriver.get().getId() == driverId) {
                throw new CarAlreadyInUseException("Car is already assigned to this user");
            }
            if (assignedDriver.isPresent()) {
                throw new CarAlreadyInUseException("Car is already assigned to user with id: " + assignedDriver.get().getId());
            }
            //assign if no exception thrown
            driverDO.setCarDO(new CarDO(carId));
        }
        else {
            if(driverDO.getCarDO() == null) {
                throw new EntityNotFoundException("The driver has not selected a car previously");
            }
            if(driverDO.getCarDO().getId() != carId) {
                throw new BusinessRuleException("The car with id: " + carId + " is not assigned to this driver");
            }
            driverDO.setCarDO(null);
        }
    }

    /**
     * Updates a driver partially.
     * @param fields The provided field values.
     * @throws EntityNotFoundException
     */
    @Override
    @Transactional
    public void updateDriverPartially(long driverId, Map<String, Object> fields) throws EntityNotFoundException {
        //not implemented yet
    }


    /**
     * Find all drivers by online state.
     *
     * @param onlineStatus
     */
    @Override
    @Transactional(readOnly = true)
    public List<DriverDO> find(OnlineStatus onlineStatus)
    {
        return driverRepository.findByOnlineStatus(onlineStatus);
    }

    /**
     * Serch for coincidences in the DB.
     * This method is mainly designed for example purposes, any
     * full-text search must include DB-specific functions
     * to optimize lookups.
     * @param filters the provided filter fields
     * @return The list of matching users
     */
    @Override
    public List<DriverDO> search(Map<String, Object> filters) {
        return super.findAll(DriverSpecifications.withFilters(filters)); // To access exception
    }


//replace with abstract method
//    private DriverDO findDriverChecked(Long driverId) throws EntityNotFoundException
//    {
//        return driverRepository.findById(driverId)
//            .orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + driverId));
//    }

    @Override
    protected JpaRepository<DriverDO, Long> getJpaRepository() {
        return driverRepository;
    }

    @Override
    protected JpaSpecificationExecutor<DriverDO> getJpaSpecificationExecutor() {
        return driverRepository;
    }

    protected CarRepository getCarRepository() {
        return carRepository;
    }
}
