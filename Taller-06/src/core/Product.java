package core;

import utils.StringFormatter;

public class Product implements IProduct{
    private final String name;
    private final double price;
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public double price() {
        return this.price;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String bill() {
        return StringFormatter.fill(new String[]{String.format("product '%s'", this.name), String.format("$%.2f", this.price)}, ' ', 75);
    }
}
