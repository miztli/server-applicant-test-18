package com.mytaxi.controller.mapper;

import com.mytaxi.datatransferobject.ManufacturerDTO;
import com.mytaxi.domainobject.ManufacturerDO;

public class ManufacturerMapper {
    public static ManufacturerDTO makeManufacturerDTO(ManufacturerDO manufacturerDO)
    {
        ManufacturerDTO.ManufacturerDTOBuilder manufacturerDTOBuilder = ManufacturerDTO.newBuilder()
                .setId(manufacturerDO.getId())
                .setName(manufacturerDO.getName())
                .setDescription(manufacturerDO.getDescription());

        return manufacturerDTOBuilder.createManufacturerDTO();
    }
}
