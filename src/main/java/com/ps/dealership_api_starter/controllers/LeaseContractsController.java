package com.ps.dealership_api_starter.controllers;

import com.ps.dealership_api_starter.data.LeaseContractDao;
import com.ps.dealership_api_starter.models.LeaseContract;
import com.ps.dealership_api_starter.models.SalesContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("lease_contracts")
@CrossOrigin
public class LeaseContractsController {

    private LeaseContractDao leaseContractDao;

    @Autowired
    public LeaseContractsController(LeaseContractDao leaseContractDao) {
        this.leaseContractDao = leaseContractDao;
    }
    @GetMapping("")
    public List<LeaseContract> getAllLeaseContracts() {
        try {
            return leaseContractDao.getAllLeaseContracts();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
    @GetMapping("{id}")
    public LeaseContract getById(@PathVariable int id) {
        try {
            var contract = leaseContractDao.getById(id);

            if (contract == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            return contract;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping()
    public LeaseContract addLeaseContract(@RequestBody LeaseContract leaseContract) {
        try {
            return leaseContractDao.create(leaseContract);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
