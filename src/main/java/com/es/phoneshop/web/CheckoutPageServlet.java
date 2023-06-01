package com.es.phoneshop.web;

import com.es.phoneshop.model.product.model.Order;
import com.es.phoneshop.model.product.service.CartService;
import com.es.phoneshop.model.product.service.OrderService;
import com.es.phoneshop.model.product.service.impl.CartServiceImpl;
import com.es.phoneshop.model.product.service.impl.OrderServiceImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CheckoutPageServlet extends HttpServlet {
    private CartService cartService;
    private OrderService orderService;

    private final String FIRST_NAME_REQUIRED_PARAM = "firstName";
    private final String LAST_NAME_REQUIRED_PARAM = "lastName";
    private final String DELIVERY_ADDRESS_REQUIRED_PARAM = "deliveryAddress";
    private final String ORDER_ATTRIBUTE = "order";
    private final String ORDER_PATH_ATTRIBUTE = "/WEB-INF/pages/checkout.jsp";
    private final String PAYMENT_METHOD_ATTRIBUTE = "selectedPaymentMethod";
    private final String ERROR_ATTRIBUTE = "possibleErrors";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CartServiceImpl.getInstance();
        orderService = OrderServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(ORDER_ATTRIBUTE,
                orderService.createOrderFromCart(cartService.getCart(request)));
        request.getRequestDispatcher(ORDER_PATH_ATTRIBUTE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Order currentOrder = orderService.createOrderFromCart(cartService.getCart(request));
        Map<String, String> possibleErrors = new HashMap<>();

        orderService.setRequiredStringCustomerInfo(
                currentOrder,
                FIRST_NAME_REQUIRED_PARAM,
                request,
                possibleErrors,
                currentOrder.getCustomerInfo()::setFirstName);

        orderService.setRequiredStringCustomerInfo(
                currentOrder,
                LAST_NAME_REQUIRED_PARAM,
                request,
                possibleErrors,
                currentOrder.getCustomerInfo()::setLastName);

        orderService.setRequiredStringCustomerInfo(
                currentOrder,
                DELIVERY_ADDRESS_REQUIRED_PARAM,
                request,
                possibleErrors,
                currentOrder.getCustomerInfo()::setDeliveryAddress);

        orderService.setRequiredPhoneNumberCustomerInfo(
                currentOrder,
                request,
                possibleErrors
        );

        orderService.setRequiredLocalDateCustomerInfo(
                currentOrder,
                request,
                possibleErrors);

        orderService.setRequiredPaymentMethodCustomerInfo(
                currentOrder,
                request,
                possibleErrors);

        if (possibleErrors.isEmpty()) {
            orderService.placeOrder(currentOrder, request);
            response.sendRedirect(String.format("%s/order/overview/%s", request.getContextPath(), currentOrder.getSecureId()));
        } else {
            request.setAttribute(ERROR_ATTRIBUTE, possibleErrors);
            request.setAttribute(ORDER_ATTRIBUTE, currentOrder);
            request.setAttribute(PAYMENT_METHOD_ATTRIBUTE, currentOrder.getCustomerInfo().getPaymentMethod().toString());
            request.getRequestDispatcher(ORDER_PATH_ATTRIBUTE).forward(request, response);
        }
    }
}
