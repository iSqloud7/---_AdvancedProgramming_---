package FirstColloquium.ColloquiumAssignments.CakeShop2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Cake extends Item {

    public Cake(String itemName) {
        super(itemName);
    }

    public Cake(String itemName, int itemPrice) {
        super(itemName, itemPrice);
    }

    @Override
    Type getType() {
        return Type.CAKE;
    }
}

class Pie extends Item {

    public Pie(String itemName) {
        super(itemName);
    }

    public Pie(String itemName, int itemPrice) {
        super(itemName, itemPrice);
    }

    @Override
    public int getItemPrice() {
        return super.getItemPrice() + 50;
    }

    @Override
    Type getType() {
        return Type.PIE;
    }
}

enum Type {

    CAKE,
    PIE
}

abstract class Item {

    private String itemName;
    private int itemPrice;

    public Item(String itemName) {
        this.itemName = itemName;
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

    abstract Type getType();

    @Override
    public String toString() {
        return "Item{" +
                "itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                '}';
    }
}

class InvalidOrderException extends Exception {

    public InvalidOrderException(int orderID) {
        super(String.format("The order with id %d has less items than the minimum allowed.",
                orderID));
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

    public static Order createOrder(String line, int minOrderItems) throws InvalidOrderException {
        String[] parts = line.split("\\s+");

        int orderID = Integer.parseInt(parts[0]);

        List<Item> items = new ArrayList<>();

        Arrays.stream(parts)
                .skip(1)
                .forEach(i -> {
                    if (Character.isAlphabetic(i.charAt(0))) {
                        if (i.charAt(0) == 'C') {
                            items.add(new Cake(i));
                        } else {
                            items.add(new Pie(i));
                        }
                    } else {
                        items.get(items.size() - 1).setItemPrice(Integer.parseInt(i));
                    }
                });

        if (items.size() < minOrderItems) {
            throw new InvalidOrderException(orderID);
        }

        return new Order(orderID, items);
    }

    public int totalItemsSum() {
        return items.stream()
                .mapToInt(i -> i.getItemPrice())
                .sum();
    }

    public int totalCakes() {
        return (int) items.stream()
                .filter(i -> i.getType().equals(Type.CAKE))
                .count();
    }

    public int totalPies() {
        return (int) items.stream()
                .filter(i -> i.getType().equals(Type.PIE))
                .count();
    }

    @Override
    public int compareTo(Order other) {
        return Integer.compare(this.totalItemsSum(), other.totalItemsSum());
    }

    @Override
    public String toString() {
        return String.format("%d %d %d %d %d\n",
                orderID, items.size(), totalPies(), totalCakes(), totalItemsSum());
    }
}

class CakeShopApplication {

    private int minOrderItems;
    private List<Order> orders;

    public CakeShopApplication() {
        this.orders = new ArrayList<>();
    }

    public CakeShopApplication(int minOrderItems) {
        this.minOrderItems = minOrderItems;
        this.orders = new ArrayList<>();
    }

    public void readCakeOrders(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        orders = reader.lines()
                .map(order -> {
                    try {
                        return Order.createOrder(order, minOrderItems);
                    } catch (InvalidOrderException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull) // or order -> order != null
                .collect(Collectors.toList());
    }

    public void printAllOrders(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);

        orders.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(order -> writer.println(order.toString()));

        writer.flush();
    }
}

public class CakeShopTest {

    public static void main(String[] args) {

        CakeShopApplication cakeShopApplication = new CakeShopApplication();

        System.out.println("===READING FROM INPUT STREAM===");
        cakeShopApplication.readCakeOrders(System.in);

        System.out.println("===PRINTING TO OUTPUT STREAM===");
        cakeShopApplication.printAllOrders(System.out);
    }
}
