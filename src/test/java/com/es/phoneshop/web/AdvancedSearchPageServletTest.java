package com.es.phoneshop.web;

import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.enums.SortField;
import com.es.phoneshop.model.product.enums.SortOrder;
import com.es.phoneshop.model.product.model.Product;
import jakarta.servlet.ServletException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class AdvancedSearchPageServletTest {
    @Mock
    private ProductDao productDao;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;

    private AdvancedSearchPageServlet servlet;

    @Before
    public void setUp() throws ServletException {
        MockitoAnnotations.initMocks(this);
        servlet = new AdvancedSearchPageServlet();
        servlet.init(servletConfig);
        when(request.getRequestDispatcher("/WEB-INF/pages/advancedSearchPage.jsp")).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoPost_withValidParameters() throws ServletException, IOException {
        String description = "test description";
        String minPrice = "100";
        String maxPrice = "200";
        Map<String, String> errorsMap = new HashMap<>();
        List<Product> foundProducts = new ArrayList<>();

        when(request.getParameter("description")).thenReturn(description);
        when(request.getParameter("minimumPrice")).thenReturn(minPrice);
        when(request.getParameter("maximumPrice")).thenReturn(maxPrice);
        when(request.getParameter("searchMethod")).thenReturn("ALL_WORDS");
        when(productDao.findProducts(description, SortField.DESCRIPTION, SortOrder.ASC, true)).thenReturn(foundProducts);
        when(productDao.findProducts(description, SortField.DESCRIPTION, SortOrder.ASC, false)).thenReturn(foundProducts);
        when(request.getAttribute("errorsMap")).thenReturn(errorsMap);

        servlet.doPost(request, response);

        verify(request).setAttribute("productList", foundProducts);
        verify(request).setAttribute("errorsMap", errorsMap);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_withInvalidMinimumPrice() throws ServletException, IOException {
        String description = "test description";
        String minPrice = "abba";
        String maxPrice = "200";
        Map<String, String> errorsMap = new HashMap<>();
        errorsMap.put("minPrice", "Not a number!");

        when(request.getParameter("description")).thenReturn(description);
        when(request.getParameter("minimumPrice")).thenReturn(minPrice);
        when(request.getParameter("maximumPrice")).thenReturn(maxPrice);
        when(request.getParameter("searchMethod")).thenReturn("ALL_WORDS");
        when(request.getAttribute("errorsMap")).thenReturn(errorsMap);

        servlet.doPost(request, response);

        verify(request).setAttribute("errorsMap", errorsMap);
        verify(requestDispatcher).forward(request, response);
    }


    @Test
    public void testDoPost_withEmptyPrice() throws ServletException, IOException {
        String description = "test description";
        String minPrice = "";
        String maxPrice = "";
        Map<String, String> errorsMap = new HashMap<>();
        List<Product> foundProducts = new ArrayList<>();

        when(request.getParameter("description")).thenReturn(description);
        when(request.getParameter("minimumPrice")).thenReturn(minPrice);
        when(request.getParameter("maximumPrice")).thenReturn(maxPrice);
        when(request.getParameter("searchMethod")).thenReturn("ANY_WORD");
        when(productDao.findProducts(description, SortField.DESCRIPTION, SortOrder.ASC, false)).thenReturn(foundProducts);
        when(request.getAttribute("errorsMap")).thenReturn(errorsMap);

        servlet.doPost(request, response);

        verify(request).setAttribute("productList", foundProducts);
        verify(request).setAttribute("errorsMap", errorsMap);
        verify(requestDispatcher).forward(request, response);
    }
}
