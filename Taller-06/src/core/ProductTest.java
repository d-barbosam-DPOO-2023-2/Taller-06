package core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    Product product;
    @BeforeEach
    void setUp() {
        product = new Product("Chicken", 100);
    }

    @Test
    void price() {
        assertEquals(100, product.price());
    }

    @Test
    void name() {
        assertEquals("Chicken", product.name());
    }

    @Test
    void bill() {
        System.out.println(product.bill());
    }
}