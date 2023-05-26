package com.es.phoneshop.service;

import com.es.phoneshop.model.product.exception.EntityNotFoundException;
import com.es.phoneshop.model.product.model.Cart;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.model.PriceHistory;
import com.es.phoneshop.model.product.model.Product;
import com.es.phoneshop.model.product.exception.OutOfStockException;
import com.es.phoneshop.model.product.service.CartService;
import com.es.phoneshop.model.product.service.impl.CartServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceImplTest {

    private CartService cartService;
    private ProductDao productDao;

    private HttpServletRequest requestMock;


    @Before
    public void init() {
        Currency usd = Currency.getInstance("USD");
        cartService = CartServiceImpl.getInstance();
        productDao = ArrayListProductDao.getInstance();
        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", new PriceHistory("11 Feb 2003", new BigDecimal(145)), new PriceHistory("12 Mar 2019", new BigDecimal(100))));

        requestMock = Mockito.mock(HttpServletRequest.class);
        HttpSession sessionMock = Mockito.mock(HttpSession.class);
        Mockito.when(requestMock.getSession()).thenReturn(sessionMock);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testAddingProductToCart() throws OutOfStockException {
        Cart cart = new Cart();
        Assert.assertTrue(cart.getItems().isEmpty());
        cartService.add(cart, 1L, 34, requestMock);
        Assert.assertFalse(cart.getItems().isEmpty());
    }

    @Test
    public void testGettingCart() {
        Cart cart = cartService.getCart(requestMock);
        Assert.assertNotNull(cart);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testAddingSameProductsIncreaseQuantityOfSameProduct() throws OutOfStockException {
        Cart cart = new Cart();
        cartService.add(cart, 1L, 10, requestMock);
        cartService.add(cart, 1L, 5, requestMock);

        Assert.assertEquals(15, cart.getItems().get(0).getQuantity());
    }

    @After
    public void clean() {
        cartService = null;
        productDao = null;
    }
}
