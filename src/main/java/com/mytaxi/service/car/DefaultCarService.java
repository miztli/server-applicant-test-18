package com.mytaxi.service.car;

import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.dataaccessobject.specifications.CarSpecifications;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.BusinessRuleException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This class handles all business rules on {@link CarDO} entities
 */
@Service
public class DefaultCarService extends AbstractService<CarDO, Long> implements CarService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCarService.class);

    private final CarRepository carRepository;

    public DefaultCarService(final CarRepository carRepository)
    {
        this.carRepository = carRepository;
    }


    /**
     * Find all cars.
     * @return The list of {@link CarDO}
     * @throws EntityNotFoundException if no cars found
     */
    @Override
    public List<CarDO> find() throws EntityNotFoundException {
        return super.findAll();
    }

    /**
     * Find a car by id
     * @param id The car's id
     * @return The {@link CarDO} fpund
     * @throws EntityNotFoundException if no car found
     */
    @Override
    @Transactional(readOnly = true)
    public CarDO find(Long id) throws EntityNotFoundException {
        return super.findById(id);
    }

    /**
     * Find a car by its assigned driver
     * @param driverId The driver's id
     * @return The {@link CarDO} found
     * @throws EntityNotFoundException if no car is assigned to user
     */
    @Override
    @Transactional(readOnly = true)
    public CarDO findByDriverId(long driverId) throws EntityNotFoundException {
        return super.findOne(CarSpecifications.findByAssignedDriver(driverId));
    }

    /**
     * Create a car
     * @param carDO The {@link CarDO} provided
     * @return The created {@link CarDO} with generated id.
     * @throws ConstraintsViolationException if some db constraints were violated
     */
    @Override
    public CarDO create(CarDO carDO) throws ConstraintsViolationException {
        return super.save(carDO);
    }

    /**
     * Delete a car by id
     * @param id The car's id
     * @throws EntityNotFoundException if the car doen't exist
     * @throws BusinessRuleException if the car is assigned currently to a driver
     */
    @Override
    public void delete(Long id) throws EntityNotFoundException, BusinessRuleException {
        CarDO carDO = super.findById(id);
        //validate if is not assigned to a driver
        if (carDO.getDriverDO() != null) {
            throw new BusinessRuleException("The car is currently assigned to a driver.");
        }
        super.deleteById(id);
    }

    @Override
    protected JpaRepository<CarDO, Long> getJpaRepository() {
        return carRepository;
    }

    @Override
    protected JpaSpecificationExecutor<CarDO> getJpaSpecificationExecutor() {
        return carRepository;
    }
}
