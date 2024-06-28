package com.ps.dealership_api_starter.data;

import com.ps.dealership_api_starter.models.SalesContract;

import java.util.List;

public interface SalesContractDao {
    List<SalesContract> getAllSalesContracts();
    SalesContract getById(int contractId);
    SalesContract create(SalesContract salesContract);
    void update(int contractId, SalesContract salesContract);
    void delete(int contractId);
}
