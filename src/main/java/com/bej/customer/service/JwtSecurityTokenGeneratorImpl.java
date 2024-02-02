package com.bej.customer.service;

import com.bej.customer.domain.Customer;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class JwtSecurityTokenGeneratorImpl implements SecurityTokenGenerator{

    //This method will generate the token

    @Override
    public Map<String, String> generateToken(Customer customer) {

        String jwtToken = null;

        jwtToken = Jwts.builder()
                .setSubject(customer.getCustomerName())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,"secretkey").compact();

        Map<String,String> map = new HashMap<>();
        map.put("token",jwtToken);
        map.put("message", "Customer Successfully logged in");
        return map;
    }
}
