package SecondColloquium.ColloquiumAssignments._3Discounts;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

class Product {

    /* Format:
       [cena_na_popust1:cena1]
                  [9169:17391]
    */

    private int discountPrice;
    private int originalPrice;

    public Product(int discountPrice, int originalPrice) {
        this.discountPrice = discountPrice;
        this.originalPrice = originalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscountPercentage() {
        return 100 - (getDiscountPrice() * 100 / getOriginalPrice());
    }

    public double getAbsoluteDiscount() {
        return getOriginalPrice() - getDiscountPrice();
    }

    @Override
    public String toString() {
        // 48% 2579/4985
        return String.format("%2d%% %d/%d",
                getDiscountPercentage(), getDiscountPrice(), getOriginalPrice());
    }
}

class Store {

    private String name;
    private List<Product> products;

    public Store(String name) {
        this.name = name;
        this.products = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public double getAverageDiscount() {
        return products.stream()
                .mapToDouble(Product::getDiscountPercentage)
                .average()
                .orElse(0);
    }

    public int getTotalDiscount() {
        return products.stream()
                .mapToInt(Product::getDiscountPrice)
                .sum();
    }

    @Override
    public String toString() {
        /* Format:
           [Store_name]
           Average discount: [заокружена вредност со едно децимално место]%
           Total discount: [вкупен апсолутен попуст]
           [процент во две места]% [цена на попуст]/[цена]
           ...

           Levis
           Average discount: 35.8%
           Total discount: 21137
           48% 2579/4985
           36% 7121/11287
           17% 6853/8314
        */

        StringBuilder builder = new StringBuilder();

        builder.append(getName())
                .append("\n");
        builder.append(String.format("Average discount: %.1f%%", getAverageDiscount()))
                .append("\n");
        builder.append(String.format("Total discount: %d", getTotalDiscount()))
                .append("\n");

        products.stream()
                .sorted(Comparator.comparing(Product::getDiscountPercentage)
                        .thenComparing(Product::getAbsoluteDiscount)
                        .reversed())
                .forEach(product -> builder.append(product)
                        .append("\n"));

        return builder.toString();
    }
}

class Discounts {

    private List<Store> stores;

    public Discounts() {
        this.stores = new ArrayList<>();
    }

    // Store: [ime] [cena_na_popust1:cena1] [cena_na_popust2:cena2] ...
    public int readStores(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            String[] parts = line.trim().split("\\s+".trim());
            Store store = new Store(parts[0]);

            Arrays.stream(parts)
                    .skip(1)
                    .map(priceString -> priceString.split(":"))
                    .map(prices -> new Product(Integer.parseInt(prices[0]), Integer.parseInt(prices[1])))
                    .forEach(store::addProduct);

            stores.add(store);
        }

        return stores.size();
    }

    public List<Store> byAverageDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::getAverageDiscount)
                        .reversed()
                        .thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::getTotalDiscount)
                        .reversed()
                        .thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }
}

public class DiscountsTest {

    public static void main(String[] args) {

        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}