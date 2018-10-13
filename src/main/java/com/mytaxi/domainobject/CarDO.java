package com.mytaxi.domainobject;

import com.mytaxi.domainvalue.EngineType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Table(name = "car")
public class CarDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated = ZonedDateTime.now();

    @Column(nullable = false)
    @NotNull(message = "License plate can not be null!")
    private String licensePlate;

    @Column(nullable = false)
    @NotNull(message = "Seat count can not be null!")
    private Integer seatCount;

    @Column
    private Boolean convertible;

    @Enumerated(EnumType.STRING)
    @Column
    private EngineType engineType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_manufacturer_id")
    private ManufacturerDO manufacturerDO;

    public CarDO() {
    }

    public CarDO(String licensePlate, Integer seatCount, Boolean convertible, EngineType engineType) {
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.engineType = engineType;
//        this.manufacturerDO = manufacturerDO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Integer getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(Integer seatCount) {
        this.seatCount = seatCount;
    }

    public Boolean getConvertible() {
        return convertible;
    }

    public void setConvertible(Boolean convertible) {
        this.convertible = convertible;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public ManufacturerDO getManufacturerDO() {
        return manufacturerDO;
    }

    public void setManufacturerDO(ManufacturerDO manufacturerDO) {
        this.manufacturerDO = manufacturerDO;
    }


}
