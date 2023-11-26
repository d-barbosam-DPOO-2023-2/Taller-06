package core;

import exceptions.ValueLimitReachedException;
import utils.StringFormatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

public class Order {
    private static final HashSet<Integer> ids = new HashSet<>();
    static {
        String absolutePath = System.getProperty("user.dir");
        File files = new File(String.format("%s/bills", absolutePath));
        for (File file: Objects.requireNonNull(files.listFiles()))
            ids.add(Integer.parseInt(file.getName().replace(".txt", "")));
    }
    private final int id;
    private final String clientName;
    private final String clientAddress;
    private final ArrayList<IProduct> products;

    public Order(String clientName, String clientAddress) {
        int id = new Random().nextInt(99999999 - 10000000 + 1) + 10000000;
        while (Order.ids.contains(id))
            id = new Random().nextInt(99999999 - 10000000 + 1) + 10000000;
        Order.ids.add(id);
        this.id = id;
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.products = new ArrayList<>();
    }

    public void close() throws IOException {
        String absolutePath = System.getProperty("user.dir");
        BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s/bills/%s.txt", absolutePath, this.id)));
        writer.write(this.bill());
        writer.close();
    }

    public int id() {
        return this.id;
    }

    public void addProduct(IProduct product) throws ValueLimitReachedException {
        double total = 0;
        for (IProduct iProduct: products)
            total += iProduct.price();
        System.out.println(total + product.price() > 150000);
        if (total + product.price() > 150_000.0f)
            throw new ValueLimitReachedException(String.format(
                    "adding the product '%s' surpass the limit of 150,000 (got %.2f)",
                    product.name(), total + product.price()));
        this.products.add(product);
    }
    public String bill() {
        double price = 0;
        String bill = "=".repeat(75) + '\n';
        bill = bill.concat(StringFormatter.fill(new String[]{"Name", "Address", "Id"}, ' ', 75, 0, 42, -1) + '\n');
        bill = bill.concat(StringFormatter.fill(new String[]{this.clientName, this.clientAddress, String.valueOf(this.id)}, ' ', 75, 0, 42, -1) + '\n');
        bill = bill.concat("=".repeat(75) + '\n');
        bill = bill.concat(StringFormatter.fill(new String[]{"Element", "Price"}, ' ', 75) + '\n');
        for (IProduct product: this.products) {
            price += product.price();
            bill = bill.concat(product.bill() + '\n');
        }
        bill = bill.concat("=".repeat(75) + '\n');
        bill = bill.concat(StringFormatter.fill(new String[]{"SUBTOTAL", String.format("$%.2f", price)}, ' ', 75) + '\n');
        bill = bill.concat(StringFormatter.fill(new String[]{"TAX 19%", String.format("$%.2f", price * 0.19)}, ' ', 75) + '\n');
        bill = bill.concat(StringFormatter.fill(new String[]{"TOTAL", String.format("$%.2f", price * 1.19)}, ' ', 75) + '\n');
        return bill.concat("=".repeat(75));
    }
}
