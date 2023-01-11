package com.ccor.springboot.backend.apirest.model.dao;

import com.ccor.springboot.backend.apirest.model.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface ICustomerDao extends CrudRepository<Customer,Long> {
}
