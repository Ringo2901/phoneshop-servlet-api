package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.exception.EntityNotFoundException;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.model.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;
    private static final String ERROR_MESSAGE = "No such product with given code";


    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Test
    public void testFindProductsWithResults() {
        String s = "s" + "ds";
        assertFalse(productDao.findProducts("", null, null).isEmpty());
    }

    @Test
    public void testSaveProduct() {
        Currency usd = Currency.getInstance("USD");
        Product newProduct = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(newProduct);
        assertTrue(newProduct.getId() > 0);
        Product testProduct = productDao.getEntity(newProduct.getId(), ERROR_MESSAGE);
        assertEquals(testProduct, newProduct);
    }

    @Test
    public void testGetProduct() {
        Product testProduct = productDao.getEntity(2L, ERROR_MESSAGE);
        assertNotNull(testProduct);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNullId() {
        productDao.getEntity(null, ERROR_MESSAGE);
    }

    @Test
    public void testSaveWithOwnIdThatDoesNotExist() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product( 4L,"sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        productDao.save(product);
        assertEquals(product, productDao.getEntity(4L, ERROR_MESSAGE));
    }

    @Test
    public void testUpdateProduct() {
        Product productBeforeUpdate = productDao.getEntity(2L, ERROR_MESSAGE);
        Product productAfterUpdate = productDao.getEntity(2L, ERROR_MESSAGE);
        productAfterUpdate.setCode("0");

        productDao.save(productAfterUpdate);

        assertEquals(productAfterUpdate.getId(), productBeforeUpdate.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteMethodWithNull() {
        productDao.delete(null);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testCorrectDeleteAndGetWithNonExistentId() {
        productDao.delete(1L);
        productDao.getEntity(1L, ERROR_MESSAGE);
    }

    @Test
    public void testNotShowingProductWithZeroStock() {
        Currency usd = Currency.getInstance("USD");
        productDao.save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));

        List<Product> testList = productDao.findProducts("", null, null);
        assertTrue(testList.stream().
                noneMatch(product -> product.getStock() == 0));
    }

    @Test
    public void testNotShowingProductWithNullPrice() {
        Currency usd = Currency.getInstance("USD");
        productDao.save(new Product("simsxg75", "Siemens SXG75", null, usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));

        List<Product> testList = productDao.findProducts("", null, null);
        assertTrue(testList.stream().
                noneMatch(product -> product.getPrice() == null));
    }

    @After
    public void clean() {
        productDao = null;
    }
}
