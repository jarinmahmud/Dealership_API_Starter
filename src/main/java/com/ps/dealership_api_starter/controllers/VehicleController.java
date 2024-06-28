package com.ps.dealership_api_starter.controllers;

import com.ps.dealership_api_starter.data.VehicleDao;
import com.ps.dealership_api_starter.models.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("vehicles")
@CrossOrigin
public class VehicleController {

    private VehicleDao vehicleDao;

    @Autowired
    public VehicleController(VehicleDao vehicleDao) {
        this.vehicleDao = vehicleDao;
    }

    @GetMapping("")
    public List<Vehicle> search(
            @RequestParam(name="minPrice", required = false) Double minPrice,
            @RequestParam(name="maxPrice", required = false) Double maxPrice,
            @RequestParam(name="make", required = false) String make,
            @RequestParam(name="model", required = false) String model,
            @RequestParam(name="minYear", required = false) Integer minYear,
            @RequestParam(name="maxYear", required = false) Integer maxYear,
            @RequestParam(name="color", required = false) String color,
            @RequestParam(name="minMiles", required = false) Integer minMiles,
            @RequestParam(name="maxMiles", required = false) Integer maxMiles,
            @RequestParam(name="type", required = false) String type
    ) {
        try {
            // Implementation for search method with filters
            return vehicleDao.search(minPrice, maxPrice, make, model, minYear, maxYear, color, minMiles, maxMiles, type);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @GetMapping("{vin}")
    public Vehicle getByVin(@PathVariable int vin) {
        try {
            var vehicle = vehicleDao.getByVin(vin);

            if (vehicle == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            return vehicle;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping()
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) {
        try {
            return vehicleDao.create(vehicle);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping("{vin}")
    public void updateVehicle(@PathVariable int vin, @RequestBody Vehicle vehicle) {
        try {
            vehicleDao.update(vin, vehicle);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @DeleteMapping("{vin}")
    public void deleteVehicle(@PathVariable int vin) {
        try {
            var vehicle = vehicleDao.getByVin(vin);

            if (vehicle == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            vehicleDao.delete(vin);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
