package com.mytaxi.service.manufacturer;

import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.dataaccessobject.ManufacturerRepository;
import com.mytaxi.dataaccessobject.specifications.CarSpecifications;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.AbstractService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultManufacturerService extends AbstractService<ManufacturerDO, Long> implements ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;
    private final CarRepository carRepository;

    public DefaultManufacturerService(ManufacturerRepository manufacturerRepository, CarRepository carRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.carRepository = carRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ManufacturerDO findCarManufacturer(long carId) throws EntityNotFoundException {

        return carRepository
                    .findOne(CarSpecifications.findByCarId(carId))
                    .orElseThrow(() -> new EntityNotFoundException("No car found with id: " + carId))
                    .getManufacturerDO();
    }

    @Override
    protected JpaRepository<ManufacturerDO, Long> getJpaRepository() {
        return manufacturerRepository;
    }

    @Override
    protected JpaSpecificationExecutor<ManufacturerDO> getJpaSpecificationExecutor() {
        return manufacturerRepository;
    }
}
