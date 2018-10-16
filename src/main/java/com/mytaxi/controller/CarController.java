package com.mytaxi.controller;

import com.mytaxi.controller.mapper.CarMapper;
import com.mytaxi.controller.mapper.ManufacturerMapper;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.datatransferobject.ManufacturerDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.BusinessRuleException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.CarService;
import com.mytaxi.service.manufacturer.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * All operations with a @{@link com.mytaxi.domainobject.CarDO}
 * will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/cars")
public class CarController {

    private final CarService carService;
    private final ManufacturerService manufacturerService;

     @Autowired
    public CarController(CarService carService, ManufacturerService manufacturerService) {
        this.carService = carService;
        this.manufacturerService = manufacturerService;
    }

    //Car mappings

    /**
     * Find all cars
     * @return The list of @{@link CarDTO}
     * @throws EntityNotFoundException if no cars found
     */
    @GetMapping
    public List<CarDTO> findCars() throws EntityNotFoundException {
        return CarMapper.makeCarDTOList(carService.find());
    }

    /**
     * Get car by id
     * @param carId
     * @return The @{@link CarDTO} found
     * @throws EntityNotFoundException if no car was found
     */
    @GetMapping("/{carId}")
    public CarDTO getCar(@PathVariable long carId) throws EntityNotFoundException
    {
        return CarMapper.makeCarDTO(carService.find(carId));
    }

    /**
     * Create a car.
     * @param carDTO
     * @return The @{@link CarDTO} found
     * @throws ConstraintsViolationException if the username already exists
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO createCar(@Valid @RequestBody CarDTO carDTO) throws ConstraintsViolationException
    {
        CarDO carDo = CarMapper.makeCarDO(carDTO);
        return CarMapper.makeCarDTO(carService.create(carDo));
    }

    /**
     * Delete a car
     * @param carId
     * @throws EntityNotFoundException
     * @throws BusinessRuleException
     */
    @DeleteMapping("/{carId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDriver(@PathVariable long carId) throws EntityNotFoundException, BusinessRuleException {
        carService.delete(carId);
    }

    //car-manufacturer mappings

    /**
     * Ger car's manufacturer.
     * @param carId
     * @return
     * @throws EntityNotFoundException
     */
    @GetMapping("/{carId}/manufacturers")
    public ManufacturerDTO getCarManufacturer(@PathVariable long carId) throws EntityNotFoundException
    {
        return ManufacturerMapper.makeManufacturerDTO(manufacturerService.findCarManufacturer(carId));
    }


}
