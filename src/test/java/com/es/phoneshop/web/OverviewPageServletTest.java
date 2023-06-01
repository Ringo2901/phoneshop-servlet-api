package com.es.phoneshop.web;

import com.es.phoneshop.model.product.dao.OrderDao;
import com.es.phoneshop.model.product.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.product.model.Order;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OverviewPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private HttpSession session;
    private OrderDao orderDao;
    private final OverviewPageServlet servlet = new OverviewPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        orderDao = ArrayListOrderDao.getInstance();
        Order orderTest = new Order();
        orderTest.setSecureId("1a-2");
        orderDao.save(orderTest);
        when(request.getPathInfo()).thenReturn("/1a-2");
    }


    @Test
    public void testDoGet() throws IOException, ServletException {
        servlet.doGet(request, response);
        verify(request).setAttribute(eq("order"), any());
    }

}
