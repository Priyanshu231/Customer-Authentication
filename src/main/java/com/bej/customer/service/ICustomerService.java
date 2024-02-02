package com.bej.customer.service;

import com.bej.customer.domain.Customer;
import com.bej.customer.exception.CustomerAlreadyExistException;
import com.bej.customer.exception.CustomerNotFoundException;

import java.util.List;

public interface ICustomerService {

    Customer saveCustomer(Customer customer) throws CustomerAlreadyExistException;
    boolean deleteCustomer(int customerId) throws CustomerNotFoundException;
    public Customer findByUsernameAndPassword(String username , String password) throws CustomerNotFoundException;
    List<Customer> getAllCustomer() throws Exception;


}
