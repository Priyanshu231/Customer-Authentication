package com.bej.customer.repository;

import com.bej.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {


    public Customer findByCustomerNameAndCustomerPassword(String customerName , String customerPassword);
}
