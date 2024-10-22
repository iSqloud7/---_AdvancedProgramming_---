package FirstColloquium.ColloquiumAssignments._25ShoppingCart;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class InvalidOperationException extends Exception {

    public InvalidOperationException(String message) {
        super(message);
    }
}

/*
WS;productID;productName;productPrice;quantity (quantity е цел број, productPrice се однесува на цена на 1 продукт)
PS;productID;productName;productPrice;quantity (quantity е децимален број - во грамови, productPrice се однесува на цена на 1 кг продукт)
*/

class Item implements Comparable<Item> {

    private String itemType;
    private String productID;
    private String productName;
    private double productPrice;
    private double quantity;

    public int getProductID() {
        return Integer.parseInt(productID);
    }

    public Item(String line) throws InvalidOperationException {
        // WS;productID;productName;productPrice;quantity
        // PS;productID;productName;productPrice;quantity
        String[] parts = line.split(";");

        this.itemType = parts[0];
        this.productID = parts[1];
        this.productName = parts[2];
        this.productPrice = Double.parseDouble(parts[3]);
        this.quantity = Double.parseDouble(parts[4]);

        if (quantity == 0) {
            // The quantity of the product with id 106512 can not be 0.
            throw new InvalidOperationException(String.format("The quantity of the product with id %s can not be 0.", productID));
        }
    }

    public double getTotalPrice() {
        if (itemType.equals("WS")) {
            return quantity * productPrice;
        }

        return quantity / 1000 * productPrice;
    }

    @Override
    public int compareTo(Item other) {
        return Double.compare(other.getTotalPrice(), this.getTotalPrice());
    }

    @Override
    public String toString() {
        // 102086 - 33552.00
        return String.format("%s - %.2f",
                productID, getTotalPrice());
    }

    public String toStringBlackFriday(int num) {
        return String.format("%s - %.2f",
                productID, getTotalPrice() / num);
    }
}

class ShoppingCart {

    private List<Item> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public void addItem(String itemData) throws InvalidOperationException {
        items.add(new Item(itemData));
    }

    public void printShoppingCart(OutputStream os) {
        PrintWriter writer = new PrintWriter(os);

        items.stream()
                .sorted()
                .forEach(item -> writer.println(item.toString()));

        writer.flush();
    }

    public void blackFridayOffer(List<Integer> discountItems, OutputStream os) throws InvalidOperationException {
        PrintWriter writer = new PrintWriter(os);

        if (discountItems.isEmpty()) {
            throw new InvalidOperationException("There are no products with discount.");
        }

        for (Item item : items) {
            for (Integer discount : discountItems) {
                if (item.getProductID() == discount) {
                    writer.println(item.toStringBlackFriday(10));
                    break;
                }
            }
        }

        writer.flush();
    }
}

public class ShoppingCartTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}