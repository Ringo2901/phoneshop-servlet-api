package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.model.Product;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsWithResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveProduct() {
        Currency usd=Currency.getInstance("USD");
        Product newProduct=new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(newProduct);
        assertTrue(newProduct.getId()>0);
        Product testProduct=productDao.getProduct(newProduct.getId());
        assertEquals(testProduct,newProduct);
    }

    @Test
    public void testGetProduct() {
        Product testProduct = productDao.getProduct(2L);
        assertNotNull(testProduct);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNullId(){
        productDao.getProduct(null);
    }

    @Test
    public void testSaveWithOwnIdThatDoesNotExist()
    {
        Currency usd=Currency.getInstance("USD");
        Product product = new Product(100L,"sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        productDao.save(product);
        assertEquals(product,productDao.getProduct(100L));
    }
    @Test
    public void testUpdateProduct() {
        Product productBeforeUpdate=productDao.getProduct(2L);
        Product productAfterUpdate=productDao.getProduct(2L);
        productAfterUpdate.setCode("0");

        productDao.save(productAfterUpdate);

        assertEquals(productAfterUpdate.getId(),productBeforeUpdate.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteMethodWithNull() {
        productDao.delete(null);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testCorrectDeleteAndGetWithNonExistentId() {
        productDao.delete(1L);
        productDao.getProduct(1L);
    }

    @Test
    public void testNotShowingProductWithZeroStock() {
        Currency usd=Currency.getInstance("USD");
        productDao.save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));

        List<Product> testList=productDao.findProducts();
        assertTrue(testList.stream().
                noneMatch(product -> product.getStock() == 0));
    }

    @Test
    public void testNotShowingProductWithNullPrice() {
        Currency usd=Currency.getInstance("USD");
        productDao.save(new Product("simsxg75", "Siemens SXG75", null, usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));

        List<Product> testList=productDao.findProducts();
        assertTrue(testList.stream().
                noneMatch(product -> product.getPrice() == null));
    }
}
