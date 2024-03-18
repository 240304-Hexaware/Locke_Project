package com.locke.babelrecords.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFilter implements Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    if ( req.getRequestURI().contains("user") ) {
      HttpServletResponse res = ((HttpServletResponse) response);
      String token = req.getHeader("AUTHORIZATION");
      if ( token != null && token.equals("redirect") ) {
        System.out.println("Lol no");
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        return;
      }
    }
    chain.doFilter(request, response);
  }

}
