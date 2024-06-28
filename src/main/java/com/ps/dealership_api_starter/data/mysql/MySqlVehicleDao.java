package com.ps.dealership_api_starter.data.mysql;

import com.ps.dealership_api_starter.data.VehicleDao;
import com.ps.dealership_api_starter.models.Vehicle;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlVehicleDao extends MySqlDaoBase implements VehicleDao {

    public MySqlVehicleDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet row = statement.executeQuery();

            while (row.next()) {
                Vehicle vehicle = mapRow(row);
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return vehicles;
    }

    @Override
    public Vehicle getByVin(int vin) {
        String sql = "SELECT * FROM vehicles WHERE vin = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, vin);
            ResultSet row = statement.executeQuery();

            if (row.next()) {
                return mapRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Vehicle create(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (vin, year, make, model, vehicle_type, color, odometer, price, sold) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, vehicle.getVin());
            statement.setInt(2, vehicle.getYear());
            statement.setString(3, vehicle.getMake());
            statement.setString(4, vehicle.getModel());
            statement.setString(5, vehicle.getVehicleType());
            statement.setString(6, vehicle.getColor());
            statement.setInt(7, vehicle.getOdometer());
            statement.setDouble(8, vehicle.getPrice());
            statement.setBoolean(9, vehicle.isSold());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return vehicle;
    }

    @Override
    public void update(int vin, Vehicle vehicle) {
        String sql = "UPDATE vehicles SET year = ?, make = ?, model = ?, vehicle_type = ?, color = ?, odometer = ?, price = ?, sold = ? WHERE vin = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, vehicle.getYear());
            statement.setString(2, vehicle.getMake());
            statement.setString(3, vehicle.getModel());
            statement.setString(4, vehicle.getVehicleType());
            statement.setString(5, vehicle.getColor());
            statement.setInt(6, vehicle.getOdometer());
            statement.setDouble(7, vehicle.getPrice());
            statement.setBoolean(8, vehicle.isSold());
            statement.setInt(9, vin);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int vin) {
        String deleteInventorySql = "DELETE FROM inventory WHERE vin = ?";
        String deleteLeaseContractsSql = "DELETE FROM lease_contracts WHERE vin = ?";
        String deleteSalesContractsSql = "DELETE FROM sales_contracts WHERE vin = ?";
        String deleteVehicleSql = "DELETE FROM vehicles WHERE vin = ?";

        try (Connection connection = getConnection()) {
            // Start transaction
            connection.setAutoCommit(false);

            try (PreparedStatement deleteInventoryStmt = connection.prepareStatement(deleteInventorySql);
                 PreparedStatement deleteLeaseContractsStmt = connection.prepareStatement(deleteLeaseContractsSql);
                 PreparedStatement deleteSalesContractsStmt = connection.prepareStatement(deleteSalesContractsSql);
                 PreparedStatement deleteVehicleStmt = connection.prepareStatement(deleteVehicleSql)) {

                // Delete related inventory records
                deleteInventoryStmt.setInt(1, vin);
                deleteInventoryStmt.executeUpdate();

                // Delete related lease contracts records
                deleteLeaseContractsStmt.setInt(1, vin);
                deleteLeaseContractsStmt.executeUpdate();

                // Delete related sales contracts records
                deleteSalesContractsStmt.setInt(1, vin);
                deleteSalesContractsStmt.executeUpdate();

                // Delete the vehicle record
                deleteVehicleStmt.setInt(1, vin);
                deleteVehicleStmt.executeUpdate();

                // Commit transaction
                connection.commit();
            } catch (SQLException e) {
                // Rollback transaction on error
                connection.rollback();
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Vehicle> search(Double minPrice, Double maxPrice, String make, String model, Integer minYear, Integer maxYear, String color, Integer minMiles, Integer maxMiles, String type) {
        List<Vehicle> vehicles = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM vehicles WHERE 1=1");

        if (minPrice != null) sql.append(" AND price >= ?");
        if (maxPrice != null) sql.append(" AND price <= ?");
        if (make != null) sql.append(" AND make = ?");
        if (model != null) sql.append(" AND model = ?");
        if (minYear != null) sql.append(" AND year >= ?");
        if (maxYear != null) sql.append(" AND year <= ?");
        if (color != null) sql.append(" AND color = ?");
        if (minMiles != null) sql.append(" AND odometer >= ?");
        if (maxMiles != null) sql.append(" AND odometer <= ?");
        if (type != null) sql.append(" AND vehicle_type = ?");

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql.toString());

            int paramIndex = 1;
            if (minPrice != null) statement.setDouble(paramIndex++, minPrice);
            if (maxPrice != null) statement.setDouble(paramIndex++, maxPrice);
            if (make != null) statement.setString(paramIndex++, make);
            if (model != null) statement.setString(paramIndex++, model);
            if (minYear != null) statement.setInt(paramIndex++, minYear);
            if (maxYear != null) statement.setInt(paramIndex++, maxYear);
            if (color != null) statement.setString(paramIndex++, color);
            if (minMiles != null) statement.setInt(paramIndex++, minMiles);
            if (maxMiles != null) statement.setInt(paramIndex++, maxMiles);
            if (type != null) statement.setString(paramIndex++, type);

            ResultSet row = statement.executeQuery();

            while (row.next()) {
                Vehicle vehicle = mapRow(row);
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return vehicles;
    }

    protected static Vehicle mapRow(ResultSet row) throws SQLException {
        int vin = row.getInt("vin");
        int year = row.getInt("year");
        String make = row.getString("make");
        String model = row.getString("model");
        String vehicleType = row.getString("vehicle_type");
        String color = row.getString("color");
        int odometer = row.getInt("odometer");
        double price = row.getDouble("price");
        boolean sold = row.getBoolean("sold");

        return new Vehicle(vin, year, make, model, vehicleType, color, odometer, price, sold);
    }
}
