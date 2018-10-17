package com.mytaxi.controller.mapper;

import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CarMapper {
    public static CarDO makeCarDO(CarDTO carDTO)
    {
        return new CarDO(carDTO.getLicensePlate(), carDTO.getSeatCount(), carDTO.getConvertible(), carDTO.getRating() , carDTO.getEngineType());
    }

    public static CarDTO makeCarDTO(CarDO carDO)
    {
        CarDTO.CarDTOBuilder carDTOBuilder = CarDTO.newBuilder()
                .setId(carDO.getId())
                .setLicensePlate(carDO.getLicensePlate())
                .setSeatCount(carDO.getSeatCount())
                .setConvertible(carDO.getConvertible())
                .setEngineType(carDO.getEngineType());

                if (carDO.getRating() != null) {
                    carDTOBuilder.setRating(carDO.getRating());
                }

        return carDTOBuilder.createCarDTO();
    }

    //only for search purposes when we need tu fill the entire object tree
    public static CarDTO makeFullCarDTO(CarDO carDO)
    {
        CarDTO.CarDTOBuilder carDTOBuilder = CarDTO.newBuilder()
                .setId(carDO.getId())
                .setLicensePlate(carDO.getLicensePlate())
                .setSeatCount(carDO.getSeatCount())
                .setConvertible(carDO.getConvertible())
                .setEngineType(carDO.getEngineType());

        if (carDO.getRating() != null) {
            carDTOBuilder.setRating(carDO.getRating());
        }

        if (carDO.getManufacturerDO() != null) {
            carDTOBuilder.setManufacturerDTO(
                    ManufacturerMapper
                            .makeManufacturerDTO(carDO.getManufacturerDO()));
        }

        return carDTOBuilder.createCarDTO();
    }


    public static List<CarDTO> makeCarDTOList(Collection<CarDO> cars)
    {
        return cars.stream()
                .map(CarMapper::makeCarDTO)
                .collect(Collectors.toList());
    }
}
