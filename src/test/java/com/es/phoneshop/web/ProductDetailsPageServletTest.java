package com.es.phoneshop.web;

import jakarta.servlet.ServletConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        when(request.getRequestDispatcher("http://localhost:8080/phoneshop-servlet-api/products/3")).thenReturn(requestDispatcher);
    }

    @Test
    public void testInit() throws ServletException, IOException {
        request.getRequestDispatcher("http://localhost:8080/phoneshop-servlet-api/products/3").forward(request, response);
        verify(servlet).init(config);
    }
}