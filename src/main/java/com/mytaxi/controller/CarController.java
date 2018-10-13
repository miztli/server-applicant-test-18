package com.mytaxi.controller;

import com.mytaxi.controller.mapper.CarMapper;
import com.mytaxi.controller.mapper.ManufacturerMapper;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.datatransferobject.ManufacturerDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.CarService;
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

    @Autowired
    public CarController(final CarService carService) {
        this.carService = carService;
    }

    //Car mappings

    @GetMapping
    public List<CarDTO> findCars()
    {
        return CarMapper.makeCarDTOList(carService.find());
    }

    @GetMapping("/{carId}")
    public CarDTO getCar(@PathVariable long carId) throws EntityNotFoundException
    {
        return CarMapper.makeCarDTO(carService.find(carId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO createDriver(@Valid @RequestBody CarDTO carDTO) throws ConstraintsViolationException
    {
        CarDO carDo = CarMapper.makeCarDO(carDTO);
        return CarMapper.makeCarDTO(carService.create(carDo));
    }

    @DeleteMapping("/{carId}")
    public void deleteDriver(@PathVariable long carId) throws EntityNotFoundException
    {
        carService.delete(carId);
    }

    //car-manufacturer mappings
    @GetMapping("/{carId}/manufacturer")
    public ManufacturerDTO getCarManufacturer(@PathVariable long carId) throws EntityNotFoundException
    {
        return ManufacturerMapper.makeManufacturerDTO(carService.findCarManufacturer(carId));
    }


}
