package com.mytaxi.controller.mapper;

import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.GeoCoordinate;
import com.mytaxi.domainvalue.OnlineStatus;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DriverMapper
{
    public static DriverDO makeDriverDO(DriverDTO driverDTO)
    {
        return new DriverDO(driverDTO.getUsername(), driverDTO.getPassword());
    }


    public static DriverDTO makeDriverDTO(DriverDO driverDO)
    {
        DriverDTO.DriverDTOBuilder driverDTOBuilder = DriverDTO.newBuilder()
            .setId(driverDO.getId())
            .setPassword(driverDO.getPassword())
            .setUsername(driverDO.getUsername());

        GeoCoordinate coordinate = driverDO.getCoordinate();
        if (coordinate != null)
        {
            driverDTOBuilder.setCoordinate(coordinate);
        }
        OnlineStatus onlineStatus = driverDO.getOnlineStatus();
        if (onlineStatus != null)
        {
            driverDTOBuilder.setOnlineStatus(onlineStatus);
        }

        return driverDTOBuilder.createDriverDTO();
    }

    //only for search purposes when we need tu fill the entire object tree
    public static DriverDTO makeFullDriverDTO(DriverDO driverDO)
    {
        DriverDTO.DriverDTOBuilder driverDTOBuilder = DriverDTO.newBuilder()
                .setId(driverDO.getId())
                .setPassword(driverDO.getPassword())
                .setUsername(driverDO.getUsername());

        GeoCoordinate coordinate = driverDO.getCoordinate();
        if (coordinate != null)
        {
            driverDTOBuilder.setCoordinate(coordinate);
        }
        OnlineStatus onlineStatus = driverDO.getOnlineStatus();
        if (onlineStatus != null)
        {
            driverDTOBuilder.setOnlineStatus(onlineStatus);
        }
        if (driverDO.getCarDO() != null) {
            driverDTOBuilder.setCarDTO(CarMapper.makeFullCarDTO(driverDO.getCarDO()));
        }

        return driverDTOBuilder.createDriverDTO();
    }

    public static List<DriverDTO> makeDriverDTOList(Collection<DriverDO> drivers)
    {
        return drivers.stream()
            .map(DriverMapper::makeDriverDTO)
            .collect(Collectors.toList());
    }

    public static List<DriverDTO> makeFullDriverDTOList(Collection<DriverDO> drivers)
    {
        return drivers.stream()
            .map(DriverMapper::makeFullDriverDTO)
            .collect(Collectors.toList());
    }
}
