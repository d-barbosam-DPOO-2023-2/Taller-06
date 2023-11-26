package core;

import exceptions.ValueLimitReachedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    Order order;

    @BeforeEach
    void setUp() {
        order = new Order("AInfinityNexus", "d.barbosam");
    }

    @Test
    void close() {
        assertDoesNotThrow(() -> order.close());
    }

    @Test
    void id() {
    }

    @Test
    void addProduct() {
        assertThrowsExactly(ValueLimitReachedException.class, () -> {
            order.addProduct(new Product("1", 100000));
            order.addProduct(new Product("2", 500000));
        });
    }

    @Test
    void bill() {
    }
}