package com.ccor.springboot.backend.apirest.controller;

import com.ccor.springboot.backend.apirest.model.entity.Customer;
import com.ccor.springboot.backend.apirest.model.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:4200"})//to connect the front and can share data with the api
@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    public ICustomerService customerService;

    @GetMapping("/customers")
    public List<Customer> findAll(){
      return customerService.findAll();
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        Customer customer = null;
        Map<String, Object> response = new HashMap<>();
        try{
            customer = customerService.findById(id);
        }catch (DataAccessException e){
           response.put("Message","Error when making the query");
           response.put("ERROR", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
           return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(customer==null) {
            response.put("Message","The customer ID:  ".concat(id.toString().concat("not exist into the database!")));
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Customer>(customer,HttpStatus.OK);
    }

    @PostMapping("/customer")
    public ResponseEntity<?> save(@Valid @RequestBody Customer customer, BindingResult result){


        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "The field '"+err.getField()+"' "+err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors",errors);
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.BAD_REQUEST);
        }

        Customer newCustomer = null;

        try{
            newCustomer = customerService.save(customer);
        }catch (DataAccessException e){
            response.put("Message","Error when making the insert");
            response.put("ERROR", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Customer>(newCustomer,HttpStatus.CREATED);


    }

    @PutMapping("/customer/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Customer customer, BindingResult result, @PathVariable("id") Long id){


        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "The field '"+err.getField()+"' not cant be empty"+err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors",errors);
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.BAD_REQUEST);
        }

        Customer currentCustomer = customerService.findById(id);
        Customer customerUpdated = null;
        if(currentCustomer==null){
            response.put("Message","Error: cannot edit the customer ID:  ".concat(id.toString()).concat("not exist into the database"));
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.NOT_FOUND);
        }

        try{
            currentCustomer.setName(customer.getName());
            currentCustomer.setLastName(customer.getLastName());
            currentCustomer.setEmail(customer.getEmail());
            currentCustomer.setCreateAt(customer.getCreateAt());
            customerUpdated = customerService.save(currentCustomer);

        }catch (DataAccessException e){
            response.put("Message","Error when making the update");
            response.put("ERROR", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(customerUpdated,HttpStatus.CREATED);


    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            customerService.delete(id);
        }catch (DataAccessException e){
            response.put("Message","Error when making the delete");
            response.put("ERROR", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("Message","The client was delete successfully");
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
    }

}
