package com.es.phoneshop.model.product.filter;

import com.es.phoneshop.web.filter.DosFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {
    @Mock
    private FilterConfig filterConfig;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    @Test
    public void testInit() throws ServletException, IOException {
        Mockito.when(request.getRemoteAddr()).thenReturn("some addr");
        DosFilter dosFilterTest = new DosFilter();
        dosFilterTest.init(filterConfig);
        dosFilterTest.doFilter(request, response, filterChain);
    }
}

