package com.bej.customer.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JwtFilter extends GenericFilter {

    /*
     * Override the doFilter method of GenericFilterBean.
     * Create HttpServletRequest , HttpServletResponse and ServletOutputStream object
     * Retrieve the "authorization" header from the HttpServletRequest object.
     * Retrieve the "Bearer" token from "authorization" header.
     * If authorization header is invalid, throw Exception with message.
     * Parse the JWT token and get claims from the token using the secret key
     * Set the request attribute with the retrieved claims
     * Call FilterChain object's doFilter() method */

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest=(HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse=(HttpServletResponse) servletResponse;
        ServletOutputStream stream=httpServletResponse.getOutputStream();

        String authHeader=httpServletRequest.getHeader("Authorization");
        if(authHeader==null || !authHeader.startsWith("Bearer"))
        {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else{
            String token=authHeader.substring(7);
            System.out.println(authHeader);
            String name=Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody().getSubject();
            httpServletRequest.setAttribute("CustomerName",name);
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }
}

