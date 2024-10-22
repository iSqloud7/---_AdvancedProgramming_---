package FirstColloquium.ColloquiumAssignments.CakeShop1;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Item {

    private String itemName;
    private int itemPrice;

    public Item(String itemName) {
        this.itemName = itemName;
        this.itemPrice = 0;
    }

    public Item(String itemName, int itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                '}';
    }
}

class Order implements Comparable<Order> {

    private int orderID;
    private List<Item> items;

    public Order() {
        this.orderID = -1;
        this.items = new ArrayList<>();
    }

    public Order(int orderID, List<Item> items) {
        this.orderID = orderID;
        this.items = items;
    }

    public static Order createOrder(String line) {
        String[] parts = line.trim().split("\\s+");
        System.out.printf("\nParsed parts: " + Arrays.toString(parts));

        int orderID = Integer.parseInt(parts[0]);

        List<Item> items = new ArrayList<>();

        Arrays.stream(parts)
                .skip(1)
                .forEach(i -> {
                    if (Character.isAlphabetic(i.charAt(0))) {
                        items.add(new Item(i));
                    } else {
                        items.get(items.size() - 1).setItemPrice(Integer.parseInt(i));
                    }
                });

        return new Order(orderID, items);
    }

    public List<Item> getItems() {
        return items;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    @Override
    public String toString() {
        return orderID + " " + items.size();
    }

    @Override
    public int compareTo(Order other) {
        return Integer.compare(this.items.size(), other.items.size());
    }
}

class CakeShopApplication {

    private List<Order> orders;

    public CakeShopApplication() {
        this.orders = new ArrayList<>();
    }

    public int readCakeOrders(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        orders = reader.lines()
                .map(Order::createOrder)
                .collect(Collectors.toList());

        return orders.stream()
                .mapToInt(i -> i.getItems().size())
                .sum();
    }

    public void printLongestOrder(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);

        Order longestOrder = orders.stream()
                .max(Comparator.naturalOrder())
                .orElseGet(Order::new);

        writer.println(longestOrder.toString());

        writer.flush();
    }
}

public class CakeShopTest {

    public static void main(String[] args) {

        /*
        CakeShopApplication cakeShopApplication = new CakeShopApplication();

        System.out.println("===READING FROM INPUT STREAM===");
        System.out.println(cakeShopApplication.readCakeOrders(System.in));

        System.out.println("===PRINTING LARGEST ORDER TO OUTPUT STREAM===");
        cakeShopApplication.printLongestOrder(System.out);
        */

        // Create a sample input stream with a known input
        String input = "42 C2 1252 C33 1029\n36 C3 330\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());

        // Create a ByteArrayOutputStream to capture the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        CakeShopApplication cakeShopApplication = new CakeShopApplication();

        System.out.println("===READING FROM INPUT STREAM===");
        int totalItems = cakeShopApplication.readCakeOrders(inputStream);
        System.out.println("\nTotal items: " + totalItems + "\n");

        System.out.println("===PRINTING LARGEST ORDER TO OUTPUT STREAM===");
        cakeShopApplication.printLongestOrder(outputStream);

        // Print the captured output
        String output = outputStream.toString();
        System.out.println("Output:\n" + output);
    }
}
