package com.es.phoneshop.service;

import com.es.phoneshop.model.product.service.ProductService;
import com.es.phoneshop.model.product.service.impl.ProductServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    private ProductService productService;
    private HttpServletRequest requestMock;

    @Before
    public void init() {
        productService = ProductServiceImpl.getInstance();
        requestMock = Mockito.mock(HttpServletRequest.class);
    }

    @Test
    public void testParsingIdFromRequest() {
        Mockito.when(requestMock.getPathInfo()).thenReturn("/3");
        Long testId = productService.parseIdWithoutHistory(requestMock);
        Assert.assertEquals(3L, (long) testId);
    }
}
