package com.es.phoneshop.web;

import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.enums.SearchType;
import com.es.phoneshop.model.product.enums.SortField;
import com.es.phoneshop.model.product.enums.SortOrder;
import com.es.phoneshop.model.product.model.Product;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdvancedSearchPageServlet extends HttpServlet {
    private ProductDao productDao;

    private static final String DESCRIPTION_PARAM = "description";
    private static final String MINIMUM_PRICE_PARAM = "minimumPrice";
    private static final String MAXIMUM_PRICE_PARAM = "maximumPrice";
    private static final String MINIMUM_PRICE_ATTR = "minPrice";
    private static final String MAXIMUM_PRICE_ATTR = "maxPrice";
    private static final String ERRORS_ATTR = "errorsMap";
    private static final String PRODUCT_LIST_ATTR = "productList";
    private static final String ERROR_NOT_A_NUMBER = "Not a number!";
    private static final String ADVANCED_SEARCH_JSP_PAGE_PATH = "/WEB-INF/pages/advancedSearchPage.jsp";
    private static final String SEARCH_METHOD_ATTR = "searchMethod";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(ADVANCED_SEARCH_JSP_PAGE_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter(DESCRIPTION_PARAM);
        String minPrice = request.getParameter(MINIMUM_PRICE_PARAM);
        String maxPrice = request.getParameter(MAXIMUM_PRICE_PARAM);
        Map<String, String> possibleErrorsMap = new HashMap<>();
        long minimumPrice = 0, maximumPrice = Long.MAX_VALUE;
        if (!minPrice.isEmpty()) {
                minimumPrice = parsePrice(possibleErrorsMap, minPrice, MINIMUM_PRICE_ATTR);
        }
        if (!maxPrice.isEmpty()) {
                maximumPrice = parsePrice(possibleErrorsMap, maxPrice, MAXIMUM_PRICE_ATTR);;
        }
        if (possibleErrorsMap.isEmpty()) {
            switch (SearchType.valueOf(request.getParameter(SEARCH_METHOD_ATTR))) {
                case ALL_WORDS:
                    List<Product> foundProducts = filterPrice(description, minimumPrice, maximumPrice, true);
                    request.setAttribute(PRODUCT_LIST_ATTR, foundProducts);
                    break;
                case ANY_WORD:
                    foundProducts = filterPrice(description, minimumPrice, maximumPrice, false);
                    request.setAttribute(PRODUCT_LIST_ATTR, foundProducts);
                    break;
            }
        }
        request.setAttribute(ERRORS_ATTR, possibleErrorsMap);
        doGet(request, response);
    }

    private Long parsePrice(Map<String, String> possibleErrorsMap, String price, String errorMessage) {
        try {
            return Long.parseLong(price);
        } catch (NumberFormatException e) {
            possibleErrorsMap.put(errorMessage, ERROR_NOT_A_NUMBER);
        }
        return 0L;
    }

    private List<Product> filterPrice(String description, long minPrice, long maxPrice, boolean lazySearch) {
        List<Product> foundProducts = productDao.findProducts(
                description,
                SortField.DESCRIPTION,
                SortOrder.ASC,
                lazySearch
        );

        return foundProducts.stream()
                .filter(product -> {
                    long price = product.getPrice().longValue();
                    return price >= minPrice && price <= maxPrice;
                })
                .collect(Collectors.toList());
    }
}
