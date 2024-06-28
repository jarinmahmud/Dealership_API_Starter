package com.ps.dealership_api_starter.controllers;

import com.ps.dealership_api_starter.data.SalesContractDao;
import com.ps.dealership_api_starter.models.SalesContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("sales_contracts")
@CrossOrigin
public class SalesContractsController {

    private SalesContractDao salesContractDao;

    @Autowired
    public SalesContractsController(SalesContractDao salesContractDao) {
        this.salesContractDao = salesContractDao;
    }
    @GetMapping("")
    public List<SalesContract> getAllSalesContracts() {
        try {
            return salesContractDao.getAllSalesContracts();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
    @GetMapping("{id}")
    public SalesContract getById(@PathVariable int id) {
        try {
            var contract = salesContractDao.getById(id);

            if (contract == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            return contract;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping()
    public SalesContract addSalesContract(@RequestBody SalesContract salesContract) {
        try {
            return salesContractDao.create(salesContract);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
