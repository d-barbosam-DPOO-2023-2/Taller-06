package core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdjustProductTest {
    AdjustProduct adjustProduct;
    Ingredient ingredient;
    @BeforeEach
    void setUp() {
        adjustProduct = new AdjustProduct(new Product("Fries", 150));
    }

    @Test
    void add() {
        adjustProduct.add(new Ingredient("lettuce", 34));
    }

    @Test
    void remove() {
        add();
        assertEquals(150 + 34, adjustProduct.price());
        adjustProduct.remove(new Ingredient("lettuce", 34));
        assertEquals(150 + 34, adjustProduct.price());
    }

    @Test
    void price() {
        add();
        assertEquals(150 + 34, adjustProduct.price());
    }

    @Test
    void name() {
        assertEquals("Fries", adjustProduct.name());
    }

    @Test
    void bill() {

    }
}