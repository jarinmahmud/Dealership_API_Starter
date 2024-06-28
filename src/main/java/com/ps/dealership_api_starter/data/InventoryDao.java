package com.ps.dealership_api_starter.data;

import com.ps.dealership_api_starter.models.Inventory;

import java.util.List;

public interface InventoryDao {
    List<Inventory> getAllInventory();
    Inventory getById(int inventoryId);
    Inventory create(Inventory inventory);
    void update(int inventoryId, Inventory inventory);
    void delete(int inventoryId);
}
