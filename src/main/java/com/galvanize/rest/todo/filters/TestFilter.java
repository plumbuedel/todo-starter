package com.galvanize.rest.todo.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * TestFilter - checks for incoming POST-Requests and returns them if the body
 * is empty
 */
@Component
public class TestFilter implements Filter {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse myResponse = (HttpServletResponse) response;

        final String method = httpRequest.getMethod();

        if (method.equals("PUT") || method.equals("POST")) {
            if (!request.getContentType().equals("application/json") || request.getContentLength() <= 0) {
                myResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "request should have json-body with content!");
                return;
            }
        }

        // System.out.println("Remote Host:" + request.getRemoteHost());
        // System.out.println("Remote Address:" + request.getRemoteHost());
        // // System.out.println("Request-Body:" + request.getReader().toString());
        // System.out.println("Request-Type:" + request.getServletContext());
        // System.out.println("Request BodyLength:" + request.getContentLengthLong());
        // System.out.println("Request...:" + request.getContentType());
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

}