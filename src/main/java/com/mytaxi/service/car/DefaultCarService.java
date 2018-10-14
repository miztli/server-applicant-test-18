package com.mytaxi.service.car;

import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.dataaccessobject.specifications.CarSpecifications;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainobject.ManufacturerDO;
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
import java.util.Optional;

@Service
public class DefaultCarService extends AbstractService<CarDO, Long> implements CarService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCarService.class);

    private final CarRepository carRepository;

    public DefaultCarService(final CarRepository carRepository)
    {
        this.carRepository = carRepository;
    }


    @Override
    public List<CarDO> find() {
        return super.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CarDO find(Long id) throws EntityNotFoundException {
        return super.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CarDO findByDriverId(long driverId) throws EntityNotFoundException {
        return super.findOne(CarSpecifications.findByAssignedDriver(driverId));
    }

    @Override
    public CarDO create(CarDO carDO) throws ConstraintsViolationException {
        return super.save(carDO);
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {
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
