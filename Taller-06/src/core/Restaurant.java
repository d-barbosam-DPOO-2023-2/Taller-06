package core;

import exceptions.RepeatedIngredientException;
import exceptions.RepeatedProductException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Restaurant {
    private final ArrayList<Product> products;
    private final ArrayList<Ingredient> ingredients;
    private final ArrayList<Combo> combos;
    private final HashMap<Integer, Order> orders;
    private Order order;
    private void loadProducts(String productPath) throws IOException, RepeatedProductException {
        BufferedReader products = new BufferedReader(new FileReader(productPath));
        for (String line: products.lines().toList()) {
            String[] info = line.split(";");
            Product newProduct = new Product(info[0], Double.parseDouble(info[1]));
            for (Product product: this.products)
                if (product.name().equals(newProduct.name()))
                    throw new RepeatedProductException(String.format("Repeated product '%s'", product.name()));
            this.products.add(newProduct);
        }
        products.close();
    }
    private void loadIngredients(String ingredientPath) throws IOException, RepeatedIngredientException {
        BufferedReader ingredients = new BufferedReader(new FileReader(ingredientPath));
        for (String line: ingredients.lines().toList()) {
            String[] info = line.split(";");
            Ingredient newIngredient = new Ingredient(info[0], Double.parseDouble(info[1]));
            for (Ingredient product: this.ingredients)
                if (product.name().equals(newIngredient.name()))
                    throw new RepeatedIngredientException(String.format("Repeated product '%s'", product.name()));
            this.ingredients.add(newIngredient);
        }
        ingredients.close();
    }
    private void loadCombos(String combosPath) throws IOException{
        BufferedReader combos = new BufferedReader(new FileReader(combosPath));
        for (String line: combos.lines().toList()) {
            String[] info = line.split(";");
            Product[] products = new Product[3];
            for (Product product: this.products)
                if (product.name().equals(info[2]))
                    products[0] = product;
                else if (product.name().equals(info[3]))
                    products[1] = product;
                else if (product.name().equals(info[4]))
                    products[2] = product;
            this.combos.add(new Combo(info[0], Double.parseDouble(info[1].replace("%", "")) / 100, products));
        }
        combos.close();
    }
    public Restaurant() {
        this.products = new ArrayList<>();
        this.ingredients = new ArrayList<>();
        this.combos = new ArrayList<>();
        this.orders = new HashMap<>();
        this.order = null;
    }
    public void load(String productPath, String ingredientPath, String combosPath)
            throws IOException, RepeatedProductException, RepeatedIngredientException {
        this.loadProducts(productPath);
        this.loadIngredients(ingredientPath);
        this.loadCombos(combosPath);
    }
    public ArrayList<Product> products() {
        return this.products;
    }
    public ArrayList<Ingredient> ingredients() {
        return this.ingredients;
    }
    public ArrayList<Combo> combos() {
        return this.combos;
    }
    public void openOrder(String clientName, String clientAddress) {
        this.order = new Order(clientName, clientAddress);
    }
    public Order order() {
        return this.order;
    }
    public Order order(int id) {
        return this.orders.get(id);
    }
    public void closeOrder() throws IOException {
        this.order.close();
        this.orders.put(this.order.id(), this.order);
        this.order = null;
    }
}
