package SecondColloquium.ColloquiumAssignments._36DeliveryApp.unfinished;

import java.util.*;

interface Location {

    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {

    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

class User {

    private String userID;
    private String userName;
    private Map<String, Location> addressesByUser;
    private double totalSpent;
    private int totalOrders;

    public User(String userID, String userName) {
        this.userID = userID;
        this.userName = userName;
        this.addressesByUser = new HashMap<>();
        this.totalSpent = 0.0;
        this.totalOrders = 0;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public Map<String, Location> getAddressesByUser() {
        return addressesByUser;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public Location getAddressName(String addressName) {
        return addressesByUser.get(addressName);
    }

    public void addSpentAmount(double amount) {
        totalSpent += amount;
    }

    public double getAverageAmountSpent() {
        return totalOrders > 0 ? getTotalSpent() / getTotalOrders() : 0.0;
    }

    public void incrementTotalOrders() {
        totalOrders++;
    }
}

class Restaurant {

    private String restaurantID;
    private String restaurantName;
    private Location currentLocation;
    private double totalIncome;
    private int orders;

    public Restaurant(String restaurantID, String restaurantName, Location currentLocation) {
        this.restaurantID = restaurantID;
        this.restaurantName = restaurantName;
        this.currentLocation = currentLocation;
        this.totalIncome = 0.0;
        this.orders = 0;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public int getOrders() {
        return orders;
    }

    public void addOrder(double cost) {
        totalIncome += cost;
        orders++;
    }

    public double getAverageOrderCost() {
        return orders > 0 ? getTotalIncome() / getOrders() : 0.0;
    }
}

class DeliveryPerson {

    private String deliveryPersonID;
    private String deliveryPersonName;
    private Location currentLocation;
    private int deliveries;
    private double earnings;

    public DeliveryPerson(String deliveryPersonID, String deliveryPersonName, Location currentLocation) {
        this.deliveryPersonID = deliveryPersonID;
        this.deliveryPersonName = deliveryPersonName;
        this.currentLocation = currentLocation;
        this.deliveries = 0;
        this.earnings = 0.0;
    }

    public String getDeliveryPersonID() {
        return deliveryPersonID;
    }

    public String getDeliveryPersonName() {
        return deliveryPersonName;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location location) {
        this.currentLocation = location;
    }

    public void addEarnings(double amount) {
        earnings += amount;
    }

    public double getEarnings() {
        return earnings;
    }

    public void increaseDeliveries() {
        deliveries++;
    }

    public int getDeliveries() {
        return deliveries;
    }

    public double getAverageDeliveryFee() {
        return deliveries > 0 ? earnings / deliveries : 0.0;
    }
}

class DeliveryApp {

    private String name;
    private Map<String, DeliveryPerson> deliveryPeopleMap;
    private Map<String, Restaurant> restaurantsMap;
    private Map<String, User> usersMap;

    public DeliveryApp(String name) {
        this.name = name;
        this.deliveryPeopleMap = new HashMap<>();
        this.restaurantsMap = new HashMap<>();
        this.usersMap = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void registerDeliveryPerson(String ID, String name, Location currentLocation) {
        deliveryPeopleMap.put(ID, new DeliveryPerson(ID, name, currentLocation));
    }

    public void addRestaurant(String ID, String name, Location location) {
        restaurantsMap.put(ID, new Restaurant(ID, name, location));
    }

    public void addUser(String ID, String name) {
        usersMap.put(ID, new User(ID, name));
    }

    public void addAddress(String ID, String addressName, Location location) {
        User user = usersMap.get(ID);

        if (user != null) {
            user.getAddressesByUser().put(addressName, location);
        }
    }

    public void orderFood(String userID, String userAddressName, String restaurantID, float cost) {
        User user = usersMap.get(userID);
        Restaurant restaurant = restaurantsMap.get(restaurantID);

        if (user != null && restaurant != null) {
            Location userLocation = user.getAddressName(userAddressName);
            if (userLocation != null) {
                DeliveryPerson closestDeliveryPerson = findClosestDeliveryPerson(restaurant.getCurrentLocation());
                if (closestDeliveryPerson != null) {
                    int distance = restaurant.getCurrentLocation().distance(userLocation);
                    double deliveryFee = 100.00; // Fixed delivery fee
                    closestDeliveryPerson.addEarnings(deliveryFee);
                    closestDeliveryPerson.increaseDeliveries();
                    closestDeliveryPerson.setCurrentLocation(userLocation);

                    user.addSpentAmount(cost);
                    user.incrementTotalOrders();
                    restaurant.addOrder(cost);
                }
            }
        }
    }

    private DeliveryPerson findClosestDeliveryPerson(Location location) {
        DeliveryPerson closestPerson = null;
        int minDistance = Integer.MAX_VALUE;

        for (DeliveryPerson deliveryPerson : deliveryPeopleMap.values()) {
            int distance = deliveryPerson.getCurrentLocation().distance(location);
            if (distance < minDistance) {
                minDistance = distance;
                closestPerson = deliveryPerson;
            } else if (distance == minDistance &&
                    deliveryPerson.getDeliveries() < closestPerson.getDeliveries()) {
                closestPerson = deliveryPerson;
            }
        }

        return closestPerson;
    }

    public void printUsers() {
        usersMap.values().stream()
                .sorted((u1, u2) -> {
                    int result = Double.compare(u2.getTotalSpent(), u1.getTotalSpent());
                    if (result == 0) {
                        result = u2.getUserID().compareTo(u1.getUserID());
                    }

                    return result;
                })
                .forEach(u -> {
                    System.out.printf("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f%n",
                            u.getUserID(),
                            u.getUserName(),
                            u.getTotalOrders(),
                            u.getTotalSpent(),
                            u.getAverageAmountSpent());
                });
    }

    public void printRestaurants() {
        restaurantsMap.values().stream()
                .sorted((r1, r2) -> {
                    int result = Double.compare(r2.getAverageOrderCost(), r1.getAverageOrderCost());
                    if (result == 0) {
                        result = r2.getRestaurantID().compareTo(r1.getRestaurantID());
                    }

                    return result;
                })
                .forEach(r -> {
                    System.out.printf("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f%n",
                            r.getRestaurantID(),
                            r.getRestaurantName(),
                            r.getOrders(),
                            r.getTotalIncome(),
                            r.getAverageOrderCost());
                });
    }

    public void printDeliveryPeople() {
        deliveryPeopleMap.values().stream()
                .sorted((dp1, dp2) -> {
                    int result = Double.compare(dp2.getEarnings(), dp1.getEarnings());
                    if (result == 0) {
                        result = dp2.getDeliveryPersonID().compareTo(dp1.getDeliveryPersonID());
                    }

                    return result;
                })
                .forEach(dp -> {
                    System.out.printf("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f%n",
                            dp.getDeliveryPersonID(),
                            dp.getDeliveryPersonName(),
                            dp.getDeliveries(),
                            dp.getEarnings(),
                            dp.getAverageDeliveryFee());
                });
    }
}

public class DeliveryAppTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}