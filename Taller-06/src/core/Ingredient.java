package core;


public class Ingredient {
    private final String name;
    private final double price;
    public Ingredient(String name, double price) {
        this.name = name;
        this.price = price;
    }
    public String name() {
        return this.name;
    }
    public double price() {
        return this.price;
    }
}
