package core;

import utils.StringFormatter;

public class Combo implements IProduct {
    private final String name;
    private final double discount;
    private final Product[] products;
    public Combo(String name, double discount, Product[] products) {
        this.name = name;
        this.discount = discount;
        this.products = products;
    }

    @Override
    public double price() {
        double price = 0;
        for (Product product: this.products)
            price += product.price();
        return price * (1 - this.discount);
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String bill() {
        double discount = 0;
        String bill = StringFormatter.fill(new String[]{String.format("combo '%s'", this.name), String.format("$%.2f", this.price())}, ' ', 75) + '\n';
        for (Product product: this.products) {
            discount += product.price() * this.discount;
            bill = bill.concat(StringFormatter.fill(new String[]{String.format("+ %s", product.name()), String.format("$%.2f", product.price())}, ' ', 75) + '\n');
        }
        return bill.concat(StringFormatter.fill(new String[]{"- discount", String.format("-$%.2f", discount)}, ' ', 75));
    }
}
