package com.bej.customer.controller;


import com.bej.customer.CustomerApplication;
import com.bej.customer.domain.Customer;
import com.bej.customer.exception.CustomerAlreadyExistException;
import com.bej.customer.exception.CustomerNotFoundException;
import com.bej.customer.service.CustomerServiceImpl;
import com.bej.customer.service.ICustomerService;
import com.bej.customer.service.SecurityTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    /*
     * Autowiring should be implemented for the CustomerServiceImpl and SecurityTokenGenerator. (Use Constructor-based
     * autowiring) Please note that we should not create an object using the new
     * keyword
     */

    private ICustomerService iCustomerService;
    private ResponseEntity responseEntity;
    private SecurityTokenGenerator securityTokenGenerator;

    @Autowired
    public CustomerController(ICustomerService iCustomerService, SecurityTokenGenerator securityTokenGenerator)
    {
        this.iCustomerService =iCustomerService;
        this.securityTokenGenerator=securityTokenGenerator;
    }


    // first step - register the customer

    /*
     * Define a handler method which will save a specific Customer by reading the
     *  object from request body and save the Customer details in the
     * database. This handler method should return any one of the status messages
     * basis on different situations:
     * 1. 201(CREATED) - If the customer saved successfully.
     * 2. 500(INTERNAL_SERVER_ERROR) - If the customer already exists
     *
     * This handler method should map to the URL "/register" using HTTP POST method
     */
    @PostMapping("/register")
    public ResponseEntity saveCustomerDetails(@RequestBody Customer customer)throws CustomerAlreadyExistException
    {
        try
        {
            iCustomerService.saveCustomer(customer);
            responseEntity=new ResponseEntity(customer,HttpStatus.CREATED);
        }
        catch(CustomerAlreadyExistException exception)
        {
            throw new CustomerAlreadyExistException();
        }
        catch(Exception exception)
        {
            responseEntity=new ResponseEntity(iCustomerService.saveCustomer(customer),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    // second step - login the customer


    /* Define a handler method which will authenticate a customer by reading the customer
     * object from request body containing the username and password. The username and password should be validated
     * before proceeding ahead with JWT token generation. The customer credentials will be validated against the database entries.
     * The error should be return if validation is not successful. If credentials are validated successfully, then JWT
     * token will be generated. The token should be returned back to the caller along with the API response.
     * This handler method should return any one of the status messages basis on different
     * situations:
     * 1. 200(OK) - If login is successful
     * 2. 401(UNAUTHORIZED) - If login is not successful
     *
     * This handler method should map to the URL "/login" using HTTP POST method
     */

    @PostMapping("/login")
    public ResponseEntity loginCustomer(@RequestBody Customer customer)throws  CustomerNotFoundException
    {

        Map<String, String> map = null;
        try {
            Customer cus = iCustomerService.findByUsernameAndPassword(customer.getCustomerName(), customer.getCustomerPassword());
            if (cus.getCustomerName().equals(customer.getCustomerName())) {
                map = securityTokenGenerator.generateToken(customer);
            }
            responseEntity = new ResponseEntity(map, HttpStatus.OK);
        }
        catch(CustomerNotFoundException e){
            throw new CustomerNotFoundException();
        }
        catch (Exception e){
            responseEntity = new ResponseEntity("Try after sometime!!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;

    }

//    Define a handler method that will delete a  Customer the handler mapping url will be "customer/{customerId}"
//    Status will be
//    1. 200(OK) - If delete is successful
//    2. 500(INTERNAL_SERVER_ERROR) - If any other error occurs

    @DeleteMapping("/customer/token/{customerId}")
    public ResponseEntity deleteCustomer(@PathVariable int customerId)throws CustomerNotFoundException
    {
        try{
            iCustomerService.deleteCustomer(customerId);
            responseEntity=new ResponseEntity<>("Successfully Deleted",HttpStatus.OK);
        }
        catch(CustomerNotFoundException exception)
        {
            throw new CustomerNotFoundException();
        }
        catch (Exception exception)
        {
            responseEntity=new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    //    Define a handler method that will get all  Customer from the database the handler mapping url will be "customer"
//    Status will be
//    1. 200(OK) - If all customers are successfully retrieved
//    2. 500(INTERNAL_SERVER_ERROR) - If any other error occurs

    @GetMapping("/customer/token")
    public ResponseEntity getAllCustomer()throws Exception
    {
        try{
            responseEntity=new ResponseEntity(iCustomerService.getAllCustomer(), HttpStatus.OK);
        }
        catch(Exception exception)
        {
            responseEntity=new ResponseEntity(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

}


