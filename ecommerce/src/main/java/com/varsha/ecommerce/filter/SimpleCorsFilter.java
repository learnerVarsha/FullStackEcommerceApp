package com.varsha.ecommerce.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.logging.Filter;

@Component
@Order(Ordered.Highest_Precedence)
public class SimpleCorsFilter implements Filter {

    @Value("${app.client.url}")
    private String clientAppUrl = "";

    public SimpleCorsFilter(){

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
}
