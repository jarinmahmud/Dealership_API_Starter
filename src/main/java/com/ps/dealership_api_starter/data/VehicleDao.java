package com.ps.dealership_api_starter.data;

import com.ps.dealership_api_starter.models.Vehicle;

import java.util.List;

public interface VehicleDao {
    List<Vehicle> getAllVehicles();
    Vehicle getByVin(int vin);
    Vehicle create(Vehicle vehicle);
    void update(int vin, Vehicle vehicle);
    void delete(int vin);
    List<Vehicle> search(Double minPrice, Double maxPrice, String make, String model, Integer minYear, Integer maxYear, String color, Integer minMiles, Integer maxMiles, String type);
}
