package com.ccor.springboot.backend.apirest.model.services;

import com.ccor.springboot.backend.apirest.model.dao.ICustomerDao;
import com.ccor.springboot.backend.apirest.model.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class CustomerServiceImpl implements ICustomerService{

    @Autowired
    private ICustomerDao  customerDao;

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return (List<Customer>) customerDao.findAll();
    }

    @Override
    @Transactional
    public Customer save(Customer customer) {
        return customerDao.save(customer);
    }

    @Override
    @Transactional
    public void delete(Long id) {
      customerDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return (Customer) customerDao.findById(id).orElse(null);
    }

}
