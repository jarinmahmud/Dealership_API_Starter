package com.ps.dealership_api_starter.data;

import com.ps.dealership_api_starter.models.LeaseContract;

import java.util.List;

public interface LeaseContractDao {
    List<LeaseContract> getAllLeaseContracts();
    LeaseContract getById(int contractId);
    LeaseContract create(LeaseContract leaseContract);
    void update(int contractId, LeaseContract leaseContract);
    void delete(int contractId);
}
