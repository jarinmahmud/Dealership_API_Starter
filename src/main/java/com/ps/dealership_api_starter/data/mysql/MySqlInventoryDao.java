package com.ps.dealership_api_starter.data.mysql;

import com.ps.dealership_api_starter.data.DealershipDao;
import com.ps.dealership_api_starter.data.InventoryDao;
import com.ps.dealership_api_starter.models.Inventory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlInventoryDao extends MySqlDaoBase implements InventoryDao {

    public MySqlInventoryDao(DataSource dataSource) {
        super(dataSource);
    }

    public List<Inventory> getAllInventory() {
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT * FROM inventory";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet row = statement.executeQuery();

            while (row.next()) {
                Inventory inventory = mapRow(row);
                inventoryList.add(inventory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return inventoryList;
    }

    public Inventory getById(int inventoryId) {
        String sql = "SELECT * FROM inventory WHERE inventory_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, inventoryId);
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

    public Inventory create(Inventory inventory) {
        String sql = "INSERT INTO inventory (dealership_id, vin) VALUES (?, ?)";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, inventory.getDealershipId());
            statement.setInt(2, inventory.getVin());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return inventory;
    }

    public void update(int inventoryId, Inventory inventory) {
        String sql = "UPDATE inventory SET dealership_id = ?, vin = ? WHERE inventory_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, inventory.getDealershipId());
            statement.setInt(2, inventory.getVin());
            statement.setInt(3, inventoryId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void delete(int inventoryId) {
        String sql = "DELETE FROM inventory WHERE inventory_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, inventoryId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected static Inventory mapRow(ResultSet row) throws SQLException {
        int inventoryId = row.getInt("inventory_id");
        int dealershipId = row.getInt("dealership_id");
        int vin = row.getInt("vin");

        return new Inventory(inventoryId, dealershipId, vin);
    }
}
