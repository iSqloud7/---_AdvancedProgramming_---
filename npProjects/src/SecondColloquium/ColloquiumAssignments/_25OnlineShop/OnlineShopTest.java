package SecondColloquium.ColloquiumAssignments._25OnlineShop;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}

class Product {

    private String productCategory;
    private String productID;
    private String productName;
    private LocalDateTime productCreatedAt;
    private double productPrice;
    private int quantitySold;

    public Product(String productCategory, String productID, String productName, LocalDateTime productCreatedAt, double productPrice) {
        this.productCategory = productCategory;
        this.productID = productID;
        this.productName = productName;
        this.productCreatedAt = productCreatedAt;
        this.productPrice = productPrice;
        this.quantitySold = 0;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public LocalDateTime getProductCreatedAt() {
        return productCreatedAt;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void incrementQuantitySold(int quantity) {
        this.quantitySold += quantity;
    }

    @Override
    public String toString() {
        /*
           PAGE 1
           Product{id='930a8e1e', name='product9', createdAt=2019-05-06T02:10:20.715710, price=1160.62, quantitySold=10}
           Product{id='389bf07c', name='product8', createdAt=2019-03-29T05:43:51.715710, price=1287.56, quantitySold=11}
           Product{id='050be27b', name='product0', createdAt=2019-01-14T23:17:46.715710, price=2913.14, quantitySold=14}
           Total revenue of the online shop is: 107031.18
           ...
        */
        return "Product{" +
                "id='" + productID + '\'' +
                ", name='" + productName + '\'' +
                ", createdAt=" + productCreatedAt +
                ", price=" + productPrice +
                ", quantitySold=" + quantitySold +
                '}';
    }
}

class OnlineShop {

    private Map<String, Product> produstsByID;

    OnlineShop() {
        this.produstsByID = new HashMap<>();
    }

    void addProduct(String category, String ID, String name, LocalDateTime createdAt, double price) {
        Product newProduct = new Product(category, ID, name, createdAt, price);

        produstsByID.put(ID, newProduct);
    }

    double buyProduct(String ID, int quantity) throws ProductNotFoundException {
        Product product = produstsByID.get(ID);
        if (product == null)
            throw new ProductNotFoundException(String.format("Product with id %s does not exist in the online shop!", ID));
        //return 0.0;

        product.incrementQuantitySold(quantity);
        return product.getProductPrice() * quantity;
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<Product> filteredProducts = produstsByID.values().stream()
                .filter(product -> category == null || product.getProductCategory().equalsIgnoreCase(category))
                .sorted(getComparator(comparatorType))
                .collect(Collectors.toList());

        List<List<Product>> pagedProducts = new ArrayList<>(); // List<List<Product>> result = new ArrayList<>();
        for (int i = 0; i < filteredProducts.size(); i += pageSize) {
            pagedProducts.add(filteredProducts.subList(i, Math.min(i + pageSize, filteredProducts.size()))); // result.add(new ArrayList<>());
        }

        return pagedProducts; // return result;
    }

    private Comparator<Product> getComparator(COMPARATOR_TYPE comparatorType) {
        switch (comparatorType) {
            case NEWEST_FIRST:
                return Comparator.comparing(Product::getProductCreatedAt).reversed();
            case OLDEST_FIRST:
                return Comparator.comparing(Product::getProductCreatedAt);
            case LOWEST_PRICE_FIRST:
                return Comparator.comparing(Product::getProductPrice);
            case HIGHEST_PRICE_FIRST:
                return Comparator.comparing(Product::getProductPrice).reversed();
            case MOST_SOLD_FIRST:
                return Comparator.comparing(Product::getQuantitySold).reversed();
            case LEAST_SOLD_FIRST:
                return Comparator.comparing(Product::getQuantitySold);
            default:
                throw new IllegalArgumentException("Unknown comparator type: " + comparatorType);
        }
    }
}

public class OnlineShopTest {

    public static void main(String[] args) {

        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category = null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}