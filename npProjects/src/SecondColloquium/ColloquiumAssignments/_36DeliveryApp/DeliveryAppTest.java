package SecondColloquium.ColloquiumAssignments._36DeliveryApp;

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

class Address {

    private final String addressName;
    private final Location addressLocation;

    public Address(String addressName, Location addressLocation) {
        this.addressName = addressName;
        this.addressLocation = addressLocation;
    }

    public String getAddressName() {
        return addressName;
    }

    public Location getAddressLocation() {
        return addressLocation;
    }
}

class User {

    private final String userID;
    private final String userName;
    private final Map<String, Address> addresses;
    private final List<Float> moneySpentList;

    public User(String userID, String userName) {
        this.userID = userID;
        this.userName = userName;
        this.addresses = new HashMap<>();
        this.moneySpentList = new ArrayList<>();
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public Map<String, Address> getAddresses() {
        return addresses;
    }

    public List<Float> getMoneySpentList() {
        return moneySpentList;
    }

    public double getTotalMoneySpent() {
        return moneySpentList.stream()
                .mapToDouble(i -> i)
                .sum();
    }

    private int getTotalOrders() {
        return moneySpentList.size();
    }

    private double getAverageMoneySpent() {
        if (getMoneySpentList().isEmpty()) // or getMoneySpentList().size() == 0
            return 0;
        return getTotalMoneySpent() / getTotalOrders();
    }

    public void addAddressToUser(String addressName, Location location) {
        addresses.put(addressName, new Address(addressName, location));
    }

    public void processOrder(float cost) {
        moneySpentList.add(cost);
    }

    @Override
    public String toString() {
        // ID: 1 Name: stefan Total orders: 1 Total amount spent: 450.00 Average amount spent: 450.00
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f",
                getUserID(),
                getUserName(),
                getTotalOrders(),
                getTotalMoneySpent(),
                getAverageMoneySpent());
    }
}

class Restaurant {

    private final String restaurantID;
    private final String restaurantName;
    private final Location restaurantLocation;
    private final List<Float> moneyEarnedList;

    public Restaurant(String restaurantID, String restaurantName, Location restaurantLocation) {
        this.restaurantID = restaurantID;
        this.restaurantName = restaurantName;
        this.restaurantLocation = restaurantLocation;
        this.moneyEarnedList = new ArrayList<>();
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public Location getRestaurantLocation() {
        return restaurantLocation;
    }

    public List<Float> getMoneyEarnedList() {
        return moneyEarnedList;
    }

    public double getTotalMoneyEarned() {
        return moneyEarnedList.stream()
                .mapToDouble(i -> i)
                .sum();
    }

    private int getTotalOrders() {
        return moneyEarnedList.size();
    }

    protected double getAverageMoneyEarned() {
        if (getMoneyEarnedList().isEmpty()) // or getMoneyEarnedList().size() == 0
            return 0;
        return getTotalMoneyEarned() / getTotalOrders();
    }

    public void processOrder(float cost) {
        moneyEarnedList.add(cost);
    }

    @Override
    public String toString() {
        // ID: 1 Name: Morino Total orders: 1 Total amount earned: 450.00 Average amount earned: 450.00
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f",
                getRestaurantID(),
                getRestaurantName(),
                getTotalOrders(),
                getTotalMoneyEarned(),
                getAverageMoneyEarned());
    }
}

class DeliveryPerson {

    private final String deliveryPersonID;
    private final String deliveryPersonName;
    private Location deliveryPersonCurrentLocation;
    private final List<Float> moneyEarnedList;

    public DeliveryPerson(String deliveryPersonID, String deliveryPersonName, Location deliveryPersonCurrentLocation) {
        this.deliveryPersonID = deliveryPersonID;
        this.deliveryPersonName = deliveryPersonName;
        this.deliveryPersonCurrentLocation = deliveryPersonCurrentLocation;
        this.moneyEarnedList = new ArrayList<>();
    }

    public String getDeliveryPersonID() {
        return deliveryPersonID;
    }

    public String getDeliveryPersonName() {
        return deliveryPersonName;
    }

    public Location getDeliveryPersonCurrentLocation() {
        return deliveryPersonCurrentLocation;
    }

    public List<Float> getMoneyEarnedList() {
        return moneyEarnedList;
    }

    public double getTotalMoneyEarned() {
        return moneyEarnedList.stream()
                .mapToDouble(i -> i)
                .sum();
    }

    private int getTotalDeliveries() {
        return moneyEarnedList.size();
    }

    private double getAverageDeliveryFee() {
        if (getMoneyEarnedList().isEmpty()) // or getMoneyEarnedList().size() == 0
            return 0;
        return getTotalMoneyEarned() / getTotalDeliveries();
    }

    public int compareDistanceToRestaurant(DeliveryPerson otherDeliveryPerson, Location restaurantLocation) {
        int myCurrentDistance = getDeliveryPersonCurrentLocation().distance(restaurantLocation); // from my current location to the restaurant's location
        int otherCurrentLocation = otherDeliveryPerson.getDeliveryPersonCurrentLocation().distance(restaurantLocation); // from otherPerson current location to the restaurant's location

        if (myCurrentDistance == otherCurrentLocation) {
            return Integer.compare(this.getTotalDeliveries(), otherDeliveryPerson.getTotalDeliveries());
        } else {
            return myCurrentDistance - otherCurrentLocation;
        }
    }

    /*
       Доставувачот заработува од нарачката така што добива 90 денари за секоја нарачка, и
       дополнителни 10 денари на секои 10 единици растојание од ресторанот до клиентот
       (пр. ако растојанието е 35 единици = 90+3х10 = 120)
    */
    public void processOrder(int distance, Location location) {
        deliveryPersonCurrentLocation = location;
        moneyEarnedList.add((float) (90 + 10 * (distance / 10)));
    }

    @Override
    public String toString() {
        // ID: 1 Name: Petko Total deliveries: 0 Total delivery fee: 0.00 Average delivery fee: 0.00
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f",
                getDeliveryPersonID(),
                getDeliveryPersonName(),
                getTotalDeliveries(),
                getTotalMoneyEarned(),
                getAverageDeliveryFee());
    }
}

class DeliveryApp {

    private final String deliveryAppName;
    private final Map<String, User> users;
    private final Map<String, Restaurant> restaurants;
    private final Map<String, DeliveryPerson> deliveryPeople;

    public DeliveryApp(String name) {
        this.deliveryAppName = name;
        this.users = new HashMap<>();
        this.restaurants = new HashMap<>();
        this.deliveryPeople = new HashMap<>();
    }

    public void addUser(String ID, String name) {
        users.put(ID, new User(ID, name));
    }

    public void addRestaurant(String ID, String name, Location location) {
        restaurants.put(ID, new Restaurant(ID, name, location));
    }

    public void registerDeliveryPerson(String ID, String name, Location currentLocation) {
        deliveryPeople.put(ID, new DeliveryPerson(ID, name, currentLocation));
    }

    public void addAddress(String ID, String addressName, Location location) {
        users.get(ID).addAddressToUser(addressName, location);
    }

    public void orderFood(String userID, String userAddressName, String restaurantID, float cost) {
        User user = users.get(userID); // метод за нарачка на храна на корисникот со ID userID
        Address address = user.getAddresses().get(userAddressName); // на неговата адреса userAddressName
        Restaurant restaurant = restaurants.get(restaurantID); // од ресторантот со ID restaurantId.

        DeliveryPerson deliveryPerson = deliveryPeople.values().stream() // При процесирање на нарачката потребно е прво да се најде доставувач кој ќе ја достави нарачката до клиентот.
                .min((left, right) -> left.compareDistanceToRestaurant(right, restaurant.getRestaurantLocation())) // Нарачката се доделува на доставувачот кој е најблиску до ресторанот.
                .get(); // Во случај да има повеќе доставувачи кои се најблиску до ресторанот - се избира доставувачот со најмалку извршени достави досега.

        /*
           По доделување на нарачката на определен доставувач,
           се менува неговата моментална локација во локацијата на клиентот кому му се доставува нарачката.
           Доставувачот заработува од нарачката така што добива 90 денари за секоја нарачка, и
           дополнителни 10 денари на секои 10 единици растојание од ресторанот до клиентот
           (пр. ако растојанието е 35 единици = 90+3х10 = 120)
        */
        user.processOrder(cost);
        restaurant.processOrder(cost);
        int distance = deliveryPerson.getDeliveryPersonCurrentLocation().distance(restaurant.getRestaurantLocation());
        deliveryPerson.processOrder(distance, address.getAddressLocation());
    }

    public void printUsers() {
        users.values().stream()
                .sorted(Comparator.comparing(User::getTotalMoneySpent)
                        .thenComparing(User::getUserID).reversed())
                .forEach(System.out::println);
    }

    public void printRestaurants() {
        restaurants.values().stream()
                .sorted(Comparator.comparing(Restaurant::getAverageMoneyEarned)
                        .thenComparing(Restaurant::getRestaurantID).reversed())
                .forEach(System.out::println);
    }

    public void printDeliveryPeople() {
        deliveryPeople.values().stream()
                .sorted(Comparator.comparing(DeliveryPerson::getTotalMoneyEarned)
                        .thenComparing(DeliveryPerson::getDeliveryPersonID).reversed())
                .forEach(System.out::println);
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