package com.mytaxi.controller;

import com.mytaxi.controller.mapper.CarMapper;
import com.mytaxi.controller.mapper.DriverMapper;
import com.mytaxi.controller.utils.EngineTypeConverter;
import com.mytaxi.controller.utils.OnlineStatusConverter;
import com.mytaxi.dataaccessobject.specifications.FilterNames;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.CarStatus;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.BusinessRuleException;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.CarService;
import com.mytaxi.service.driver.DriverService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * All operations with a driver will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/drivers")
public class DriverController
{

    private final DriverService driverService;
    private final CarService carService;

    @Autowired
    public DriverController(final DriverService driverService, final CarService carService)
    {
        this.driverService = driverService;
        this.carService = carService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(EngineType.class, new EngineTypeConverter());
        binder.registerCustomEditor(EngineType.class, new OnlineStatusConverter());
    }

    // DRIVER MAPPINGS

    @GetMapping("/{driverId}")
    public DriverDTO getDriver(@PathVariable long driverId) throws EntityNotFoundException
    {
        return DriverMapper.makeDriverDTO(driverService.find(driverId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDTO createDriver(@Valid @RequestBody DriverDTO driverDTO) throws ConstraintsViolationException
    {
        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);
        return DriverMapper.makeDriverDTO(driverService.create(driverDO));
    }

    @DeleteMapping("/{driverId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDriver(@PathVariable long driverId) throws EntityNotFoundException
    {
        driverService.delete(driverId);
    }

    /**
     * Changed PutMapping annotation to have more control over
     * url mappings, using request params to define which controller method
     * to apply.
     * @param driverId
     * @param longitude
     * @param latitude
     * @throws EntityNotFoundException
     */
//    @PutMapping("/{driverId}")
    @RequestMapping(
            value = "/{driverId}",
            params = {"longitude", "latitude"},
            method = RequestMethod.PUT
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLocation(@PathVariable long driverId,
                               @RequestParam double longitude,
                               @RequestParam double latitude)
        throws EntityNotFoundException
    {
        driverService.updateLocation(driverId, longitude, latitude);
    }

    /**
     * Changed PutMapping annotation to have more control over
     * url mappings, using request params to define which controller method
     * to apply.
     * @param driverId The driver's id
     * @throws EntityNotFoundException if no driver found
     */
//    @PutMapping("/{driverId}")
    @RequestMapping(
            value = "/{driverId}",
            params = {"onlineStatus"},
            method = RequestMethod.PUT
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDriverStatus(@PathVariable long driverId,
                                   @RequestParam OnlineStatus onlineStatus)
        throws EntityNotFoundException
    {
        driverService.updateOnlineStatus(driverId, onlineStatus);
    }

    /**
     * Updates a driver partially (using request parameters).
     * Although this goes against convention for a PUT method,
     * we still have type validation at the controller layer.
     * @param driverId
     * @param carId
     * @param selected
     * @throws EntityNotFoundException
     */
    @RequestMapping(
            value = "/{driverId}",
            params = {"carId", "selected"},
            method = RequestMethod.PUT
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDriverCarTest(@PathVariable long driverId,
                                    @RequestParam long carId,
                                    @RequestParam boolean selected)
            throws EntityNotFoundException, CarAlreadyInUseException, BusinessRuleException {
        driverService.updateDriverCar(driverId, carId, selected);
    }

    /**
     * Updates a driver partially (using request body).
     *
     * Typically I use patch (for convention), since only few fields of
     * the resource will be changed.
     * One great disadvantage is that validation is not done at the Controller
     * Layer but elsewhere f.e. in Service Layer.
     * @param driverId The id of the driver.
     * @param fields Only the desired fields of the {@link DriverDO} that must change.
     * @throws EntityNotFoundException If driver with provided id is not in the DB.
     */
    @PatchMapping("/{driverId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDriverPartially(
            @PathVariable long driverId, @RequestBody Map<String, Object> fields)
        throws EntityNotFoundException
    {
        driverService.updateDriverPartially(driverId, fields); //not implemented, yet
    }

    @RequestMapping(
            params = {"onlineStatus"},
            method = RequestMethod.GET
    )
    public List<DriverDTO> findDrivers(@RequestParam OnlineStatus onlineStatus)
    {
        return DriverMapper.makeDriverDTOList(driverService.find(onlineStatus));
    }

    @RequestMapping(
            method = RequestMethod.GET
    )
    public List<DriverDTO> findAllDrivers()
    {
        return DriverMapper.makeDriverDTOList(driverService.findAll());
    }

    /**
     * Search for matching drivers with the provided params.
     * @param username
     * @param onlineStatus
     * @param rating
     * @param seatCount
     * @param engineType
     * @param convertible
     * @param manufacturerName
     * @return
     */
    @GetMapping("/search")
    public List<DriverDTO> findMatchingDrivers(@RequestParam(required = false) String username,
                                               @RequestParam(required = false) OnlineStatus onlineStatus,
                                               @RequestParam(required = false) Integer rating,
                                               @RequestParam(required = false) String licensePlate,
                                               @RequestParam(required = false) Integer seatCount,
                                               @RequestParam(required = false) EngineType engineType,
                                               @RequestParam(required = false) Boolean convertible,
                                               @RequestParam(required = false) String manufacturerName
                                               ) {

        Map<String, Object> filters = new HashMap<>();
            if (username != null) { filters.put(FilterNames.FIELD_USER_NAME, username); }
            if (onlineStatus != null) { filters.put(FilterNames.FIELD_ONLINE_STATUS, onlineStatus); }
            if (rating != null) { filters.put(FilterNames.FIELD_RATING, rating); }
            if (licensePlate != null) { filters.put(FilterNames.FIELD_LICENSE_PLATE, licensePlate); }
            if (seatCount != null) { filters.put(FilterNames.FIELD_SEAT_COUNT, seatCount); }
            if (engineType != null) { filters.put(FilterNames.FIELD_ENGINE_TYPE, engineType); }
            if (convertible != null) { filters.put(FilterNames.FIELD_CONVERTIBLE, convertible); }
            if (manufacturerName != null) { filters.put(FilterNames.FIELD_MANUFACTURER_NAME, manufacturerName); }

        return DriverMapper.makeDriverDTOList(driverService.search(filters));
    }

    // CAR MAPPINGS

    @GetMapping("/{driverId}/car")
    public CarDTO getDriverCar(@PathVariable long driverId) throws EntityNotFoundException
    {
        return CarMapper.makeCarDTO(carService.findByDriverId(driverId));
    }
}
