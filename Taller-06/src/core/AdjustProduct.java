package core;

import utils.StringFormatter;

import java.util.ArrayList;

public class AdjustProduct implements IProduct{
    private final IProduct original;
    private final ArrayList<Ingredient> added;
    private final ArrayList<Ingredient> removed;

    public AdjustProduct(IProduct original) {
        this.original = original;
        this.added = new ArrayList<>();
        this.removed = new ArrayList<>();
    }

    public void add(Ingredient ingredient) {
        this.added.add(ingredient);
    }
    public void remove(Ingredient ingredient) {
        this.removed.add(ingredient);
    }

    @Override
    public double price() {
        double price = this.original.price();
        for (Ingredient ingredient: this.added)
            price += ingredient.price();
        return price;
    }

    @Override
    public String name() {
        return this.original.name();
    }

    @Override
    public String bill() {
        String bill = StringFormatter.fill(new String[]{String.format("product '%s'", this.name()), String.format("$%.2f", this.original.price())}, ' ', 75) + '\n';
        for (Ingredient ingredient: this.added)
            bill = bill.concat(StringFormatter.fill(new String[]{String.format("+ '%s'", ingredient.name()), String.format("$%.2f", ingredient.price())}, ' ', 75) + '\n');
        for (Ingredient ingredient: this.removed)
            bill = bill.concat(StringFormatter.fill(new String[]{String.format("- '%s'", ingredient.name()), "-$0.00"}, ' ', 75) + '\n');
        return bill.trim();
    }
}
