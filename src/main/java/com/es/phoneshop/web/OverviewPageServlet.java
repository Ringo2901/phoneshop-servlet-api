package com.es.phoneshop.web;

import com.es.phoneshop.model.product.dao.OrderDao;
import com.es.phoneshop.model.product.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.product.service.OrderService;
import com.es.phoneshop.model.product.service.impl.OrderServiceImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class OverviewPageServlet extends HttpServlet {
    private OrderDao orderDao;
    private OrderService orderService;
    private static final String ORDER_ATTRIBUTE = "order";
    private static final String ORDER_PATH_ATTRIBUTE = "/WEB-INF/pages/overview.jsp";


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderService = OrderServiceImpl.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(ORDER_ATTRIBUTE,
                orderDao.getOrderBySecureId(orderService.parseSecureOrderIdFromRequest(request)));
        request.getRequestDispatcher(ORDER_PATH_ATTRIBUTE).forward(request, response);
    }
}
