package com.mytaxi.service.manufacturer;

import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.dataaccessobject.ManufacturerRepository;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.ManufacturerDO;
import com.mytaxi.service.AbstractService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

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
    public ManufacturerDO findCarManufacturer(long carId) {
        CarDO carDO = carRepository
                        .findById(carId)
                        .orElseThrow(()->new EntityNotFoundException("No car found with id: " + carId));
        if (carDO.getManufacturerDO()!=null) {
            ManufacturerDO manufacturerDTO = carDO.getManufacturerDO();
            return manufacturerDTO;
        }
        else {
            throw new EntityNotFoundException("No manufacturer found for car with id: " + carId);
        }
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
