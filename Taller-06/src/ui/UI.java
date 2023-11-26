package ui;

import core.*;

import java.io.*;

import java.util.ArrayList;

import exceptions.RepeatedIngredientException;
import exceptions.RepeatedProductException;
import exceptions.ValueLimitReachedException;
import utils.StringFormatter;


public class UI {
    public static void main(String[] args) throws IOException {
        Restaurant restaurant = new Restaurant();
        String absolutePath = System.getProperty("user.dir") + "/data/";
        try {
            restaurant.load(absolutePath + "products.txt", absolutePath + "ingredients.txt", absolutePath + "combos.txt");
        } catch (IOException ignored) {
            UI.print("something wrong while extracting information from database");
            return;
        } catch (RepeatedProductException | RepeatedIngredientException error) {
            UI.print(error.getMessage());
            return;
        }
        UI.print("=".repeat(75));
        print(StringFormatter.padding(" Welcome to Culinary Haven ", '=', 75, StringFormatter.Alignment.MID));
        UI.print("=".repeat(75));
        boolean running = true;
        while (running) {
            UI.print();
            UI.print("1. Display the menu.");
            UI.print("2. Initiate a new order.");
            UI.print("3. Add an item to an order.");
            UI.print("4. Close an order and save the invoice.");
            UI.print("5. Retrieve information about an order based on its ID.");
            UI.print("6. Exit the program.");
            UI.print();
            String input;
            while (!UI.isDecimal(input = UI.input("Select an option: ")))
                UI.print("Please enter a valid integer value.\n");
            UI.print();
            int option = Integer.parseInt(input);
            if (option == 1)
                UI.displayMenu(restaurant);
            else if (option == 2)
                UI.initializeOrder(restaurant);
            else if (option == 3)
                UI.addItemToOrder(restaurant);
            else if (option == 4)
                UI.completeOrder(restaurant);
            else if (option == 5)
                UI.retrieveInfoByID(restaurant);
            else if (option == 6)
                running = false;
            else
                UI.print("Invalid option " + option + ", please enter a valid option.\n");
        }
        UI.print("=".repeat(75));
        print(StringFormatter.padding(" Powered by: AInfinityNexus ", '=', 75, StringFormatter.Alignment.MID));
        UI.print("=".repeat(75));
    }

    private static void displayMenu(Restaurant restaurant) {
        ArrayList<Product> products = restaurant.products();
        ArrayList<Combo> combos = restaurant.combos();
        print(StringFormatter.padding(" 1. Products ", '=', 75, StringFormatter.Alignment.MID));
        print(StringFormatter.fill(new String[]{"Name", "Price"}, ' ', 75));
        for (int index = 0; index < products.size(); index++) {
            Product product = products.get(index);
            print(StringFormatter.fill(new String[]{String.format("%s. %s ", index + 1, product.name()), String.format(" $%.2f", product.price())}, ' ', 75));
        }
        print(StringFormatter.padding(" 2. Combos ", '=', 75, StringFormatter.Alignment.MID));
        print(StringFormatter.fill(new String[]{"Name", "Price"}, ' ', 75));
        for (int index = 0; index < combos.size(); index++) {
            Combo product = combos.get(index);
            print(StringFormatter.fill(new String[]{String.format("%s. %s ", index + 1, product.name()), String.format(" $%.2f", product.price())}, ' ', 75));
        }
        UI.print("=".repeat(75));
    }

    private static void initializeOrder(Restaurant restaurant) throws IOException {
        if (restaurant.order() == null) {
            String clientName;
            while ((clientName = UI.input("Please enter the client's name: ")).length() > 40)
                UI.print("Names longer than 40 characters are not permitted.\n");
            String clientAddress;
            while ((clientAddress = UI.input("Please enter the client's address: ")).length() > 23)
                UI.print("Addresses longer than 23 characters are not permitted.\n");
            restaurant.openOrder(clientName, clientAddress);
            UI.print();
            UI.print("Order initiated with the following information: ");
            UI.print("=".repeat(75));
            print(StringFormatter.fill(new String[]{"Name", "Address", "Id"}, ' ', 75, 0, 42, -1));
            print(StringFormatter.fill(new String[]{clientName, clientAddress, String.valueOf(restaurant.order().id())}, ' ', 75, 0, 42, -1));
            UI.print("=".repeat(75));
        }
        else
            UI.print("An order is already in progress. Please complete or close the existing order.");
    }
    private static AdjustProduct editIngredients(Restaurant restaurant, AdjustProduct product) throws IOException {
        ArrayList<Ingredient> ingredients = restaurant.ingredients();
        print(StringFormatter.padding(" Ingredients ", '=', 75, StringFormatter.Alignment.MID));
        print(StringFormatter.fill(new String[]{"Name", "Price"}, ' ', 75));
        for (int index = 0; index < ingredients.size(); index++) {
            Ingredient ingredient = ingredients.get(index);
            print(StringFormatter.fill(new String[]{String.format("%s. %s ", index + 1, ingredient.name()), String.format(" $%.2f", ingredient.price())}, ' ', 75));
        }
        UI.print("=".repeat(75));
        UI.print();
        String input;
        while (!UI.isDecimal(input = UI.input("Please enter the ingredient number: ")))
            UI.print("Please enter a valid integer value.\n");
        UI.print();
        int option = Integer.parseInt(input) - 1;
        if (0 > option || option >= ingredients.size()) {
            UI.print("Invalid option " + option + ", please enter a valid option.");
            UI.editIngredients(restaurant, product);
        }
        Ingredient ingredient = restaurant.ingredients().get(option);
        input = UI.input(String.format("Do you wish to add or remove the ingredient '%s'? (A/R): ", ingredient.name()));
        while ((!input.equals("A") && !input.equals("R"))) {
            UI.print(String.format("Invalid option '%s', please select a valid option.\n", input));
            input = UI.input(String.format("Do you wish to add or remove the ingredient '%s'? (A/R): ", ingredient.name()));
        }
        if (input.equals("A")) {
            product.add(ingredient);
            UI.print(String.format("\nThe ingredient '%s' has been added successfully.", product.name()));
        }
        else {
            product.remove(ingredient);
            UI.print(String.format("\nThe ingredient '%s' has been removed successfully.", product.name()));
        }
        UI.print("=".repeat(75));
        UI.print(product.bill());
        UI.print("=".repeat(75));
        UI.print();
        input = UI.input("Do you wish to add or remove more ingredients? (Y/N): ");
        while ((!input.equals("Y") && !input.equals("N"))) {
            UI.print(String.format("Invalid option '%s', please select a valid option.\n", input));
            input = UI.input("Do you wish to add or remove more ingredients? (Y/N): ");
        }
        UI.print();
        return input.equals("Y") ? UI.editIngredients(restaurant, product) : product;
    }
    private static void addProduct(Restaurant restaurant) throws IOException {
        UI.print();
        ArrayList<Product> products = restaurant.products();
        print(StringFormatter.padding(" Products ", '=', 75, StringFormatter.Alignment.MID));
        print(StringFormatter.fill(new String[]{"Name", "Price"}, ' ', 75));
        for (int index = 0; index < products.size(); index++) {
            Product product = products.get(index);
            print(StringFormatter.fill(new String[]{String.format("%s. %s ", index + 1, product.name()), String.format(" $%.2f", product.price())}, ' ', 75));
        }
        UI.print("=".repeat(75));
        UI.print();
        String input;
        while (!UI.isDecimal(input = UI.input("Please enter the product number: ")))
            UI.print("Please enter a valid integer value.\n");
        int option = Integer.parseInt(input) - 1;
        if (0 > option || option >= products.size()) {
            UI.print("Invalid option " + option + ", please enter a valid option.");
            UI.addProduct(restaurant);
        }
        UI.print();
        input = UI.input("Would you like to add or remove ingredients? (Y/N): ");
        while (!input.equals("Y") && !input.equals("N")) {
            print(String.format("Invalid option '%s', please select a valid option.\n", input));
            input = UI.input("Would you like to add or remove ingredients? (A/R/N): ");
        }
        UI.print();
        IProduct product = input.equals("Y") ? editIngredients(restaurant, new AdjustProduct(restaurant.products().get(option))) : restaurant.products().get(option);
        input = UI.input(String.format("Do you wish to add the product '%s'? (Y/N): ", product.name()));
        while ((!input.equals("Y") && !input.equals("N"))) {
            UI.print(String.format("Invalid option '%s', please select a valid option.\n", input));
            input = UI.input(String.format("Do you wish to add the product '%s'? (Y/N): ", product.name()));
        }
        if (input.equals("Y")) {
            try {
                restaurant.order().addProduct(product);
            } catch (ValueLimitReachedException error) {
                UI.print(error.getMessage());
                return;
            }
            UI.print(String.format("\nThe product '%s' has been added successfully.", product.name()));
            UI.print("=".repeat(75));
            UI.print(product.bill());
            UI.print("=".repeat(75));
        }
        else
            UI.print(String.format("\nThe product '%s' has not been included.", product.name()));
    }
    private static void addCombo(Restaurant restaurant) throws IOException {
        UI.print();
        ArrayList<Combo> combos = restaurant.combos();
        print(StringFormatter.padding(" Combos ", '=', 75, StringFormatter.Alignment.MID));
        print(StringFormatter.fill(new String[]{"Name", "Price"}, ' ', 75));
        for (int index = 0; index < combos.size(); index++) {
            Combo combo = combos.get(index);
            print(StringFormatter.fill(new String[]{String.format("%s. %s ", index + 1, combo.name()), String.format(" $%.2f", combo.price())}, ' ', 75));
        }
        UI.print("=".repeat(75));
        UI.print();
        String input;
        while (!UI.isDecimal(input = UI.input("Please enter the combo number: ")))
            UI.print("Please enter a valid integer value.\n");
        int option = Integer.parseInt(input) - 1;
        if (0 > option || option >= combos.size()) {
            UI.print("Invalid option " + option + ", please enter a valid option.");
            UI.addProduct(restaurant);
        }
        Combo combo = combos.get(option);
        UI.print();
        input = UI.input(String.format("Do you wish to add the combo '%s'? (Y/N): ", combo.name()));
        while ((!input.equals("Y") && !input.equals("N"))) {
            UI.print(String.format("Invalid option '%s', please select a valid option.\n", input));
            input = UI.input(String.format("Do you wish to add the combo '%s'? (Y/N): ", combo.name()));
        }
        if (input.equals("Y")) {
            try {
                restaurant.order().addProduct(combo);
            } catch (ValueLimitReachedException error) {
                UI.print(error.getMessage());
                return;
            }
            UI.print(String.format("\nThe combo '%s' has been added successfully.", combo.name()));
            UI.print("=".repeat(75));
            UI.print(combo.bill());
            UI.print("=".repeat(75));
        }
        else
            UI.print(String.format("\nThe combo '%s' has not been included.", combo.name()));
    }
    private static void addItemToOrder(Restaurant restaurant) throws IOException {
        if (restaurant.order() != null) {
            UI.print("1. Products");
            UI.print("2. Combos");
            String input;
            while (!UI.isDecimal(input = UI.input("Please select one of the options: ")))
                UI.print("Please enter a valid integer value.\n");
            int option = Integer.parseInt(input);
            if (1 > option || option > 2) {
                UI.print("Invalid option " + option + ", please enter a valid option.\n");
                UI.addItemToOrder(restaurant);
            }
            if (option == 1)
                addProduct(restaurant);
            else if (option == 2)
                addCombo(restaurant);
        }
        else
            UI.print("There are currently no open orders to add items to.");
    }

    private static void completeOrder(Restaurant restaurant) throws IOException{
        if (restaurant.order() != null) {
            Order order = restaurant.order();
            restaurant.closeOrder();
            print(order.bill());
        }
        else
            UI.print("There are currently no open orders to close.");
    }
    private static void retrieveInfoByID(Restaurant restaurant) throws IOException {
        String input;
        while (!UI.isDecimal(input = UI.input("Please enter the order ID: ")))
            UI.print("Please enter a valid integer value.\n");
        int id = Integer.parseInt(input);
        Order order = restaurant.order(id);
        if (order != null)
            UI.print(order.bill());
        else {
            String absolutePath = System.getProperty("user.dir");
            try {
                BufferedReader reader = new BufferedReader(new FileReader(String.format("%s/bills/%s.txt", absolutePath, id)));
                String bill = "";
                for (String line: reader.lines().toList())
                    bill = bill.concat(line + '\n');
                UI.print(bill);
                reader.close();
            } catch (FileNotFoundException ignored) {
                UI.print(String.format("The order with ID '%s' is not found in the system.", id));
            }
        }
    }
    private static boolean isDecimal(String string) {
        if (string == null || string.isEmpty())
            return false;
        for (char c: string.toCharArray())
            if (c != '-')
                if (!Character.isDigit(c))
                    return false;
        return true;
    }

    private static String input(String prompt) throws IOException {
        System.out.print(prompt);
        return new BufferedReader(new InputStreamReader(System.in)).readLine();
    }

    private static void print(Object ...values) {
        for (Object value: values)
            System.out.print(value + " ");
        System.out.println();
    }

}
