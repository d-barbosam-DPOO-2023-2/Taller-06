package core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComboTest {
    Combo combo;
    @BeforeEach
    void setUp() {
        combo = new Combo("Burger and Fires", .10f,
                new Product[]{new Product("Burger", 15000), new Product("Fires", 3000)});
    }

    @Test
    void price() {
        assertEquals((15000 + 3000) * 0.9, Math.round(combo.price()));
    }

}