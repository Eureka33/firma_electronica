package com.eureka.firma.digital.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class RequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init request filter");
    }
   
    @Override
    public void doFilter( ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException { 
        final HttpServletRequest request = (HttpServletRequest) req;
        
        request.setCharacterEncoding( "UTF-8");
        
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        System.out.println( "destroy request filter");
    }
    
}
