package SecondColloquium.ColloquiumAssignments._14CarCollection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Car implements Comparable<Car> {

    private String manufacturer;
    private String model;
    private int price;
    private float power;

    public Car(String manufacturer, String model, int price, float power) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;
        this.power = power;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public int getPrice() {
        return price;
    }

    public float getPower() {
        return power;
    }

    @Override
    public int compareTo(Car other) {
        return this.getModel().compareTo(other.getModel());
    }

    @Override
    public String toString() {
        // Renault Clio (96KW) 12100
        return String.format("%s %s (%.0fKW) %d",
                getManufacturer(),
                getModel(),
                getPower(),
                getPrice());
    }
}

class CarCollection {

    private List<Car> cars;

    public CarCollection() {
        this.cars = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void sortByPrice(boolean ascending) {
        if (ascending) { // true
            cars = cars.stream()
                    .sorted((c1, c2) -> {
                        int result = Integer.compare(c1.getPrice(), c2.getPrice());
                        if (result == 0) {
                            return Float.compare(c1.getPower(), c2.getPower());
                        }

                        return result;
                    })
                    .collect(Collectors.toList());
        } else { // false
            cars = cars.stream()
                    .sorted((c1, c2) -> {
                        int result = Integer.compare(c2.getPrice(), c1.getPrice());
                        if (result == 0) {
                            return Float.compare(c2.getPower(), c1.getPower());
                        }

                        return result;
                    })
                    .collect(Collectors.toList());
        }
    }

    public List<Car> filterByManufacturer(String manufacturer) {
        return cars.stream()
                .filter(car -> car.getManufacturer().equalsIgnoreCase(manufacturer))
                .sorted(Comparator.comparing(Car::getModel))
                .collect(Collectors.toList());
    }

    public List<Car> getList() {
        return new ArrayList<>(cars);
    }
}

public class CarCollectionTest {

    public static void main(String[] args) {

        CarCollection carCollection = new CarCollection();
        String manufacturer = fillCollection(carCollection);
        carCollection.sortByPrice(true);
        System.out.println("=== Sorted By Price ASC ===");
        print(carCollection.getList());
        carCollection.sortByPrice(false);
        System.out.println("=== Sorted By Price DESC ===");
        print(carCollection.getList());
        System.out.printf("=== Filtered By Manufacturer: %s ===\n", manufacturer);
        List<Car> result = carCollection.filterByManufacturer(manufacturer);
        print(result);
    }

    static void print(List<Car> cars) {
        for (Car c : cars) {
            System.out.println(c);
        }
    }

    static String fillCollection(CarCollection cc) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            if (parts.length < 4) return parts[0];
            Car car = new Car(parts[0], parts[1], Integer.parseInt(parts[2]),
                    Float.parseFloat(parts[3]));
            cc.addCar(car);
        }
        scanner.close();
        return "";
    }
}