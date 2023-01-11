package com.ccor.springboot.backend.apirest.model.services;

import com.ccor.springboot.backend.apirest.model.entity.Customer;

import java.util.List;

public interface ICustomerService {

    public List<Customer> findAll();
    public Customer save(Customer customer);
    public void delete(Long id);
    public Customer findById(Long id);
}

