// Package declaration
package FirstColloquium.ColloquiumAssignments.Container.AllInOne;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

// Interface that defines an object that has a weight and can be compared based on that weight
interface Weightable extends Comparable<Weightable> {

    // Method to get the weight of the object
    public double getWeight();

    // Default implementation of compareTo based on the weight
    @Override
    default int compareTo(Weightable other) {
        return Double.compare(this.getWeight(), other.getWeight());
    }
}

// Class representing a double value that implements the Weightable interface
class WeightableDouble implements Weightable {

    private double weight; // The weight value

    // Constructor that initializes the weight
    public WeightableDouble(double weight) {
        this.weight = weight;
    }

    // Implementation of the getWeight method from the Weightable interface
    @Override
    public double getWeight() {
        return weight;
    }
}

// Class representing a string that implements the Weightable interface
class WeightableString implements Weightable {

    private String word; // The string value

    // Constructor that initializes the string
    public WeightableString(String word) {
        this.word = word;
    }

    // Implementation of the getWeight method from the Weightable interface
    // Here, the weight is defined as the length of the string
    @Override
    public double getWeight() {
        return word.length();
    }
}

// Generic class that can contain elements of any type that implements the Weightable interface
class Container<T extends Weightable> {

    private List<T> elements; // List to store the elements

    // Constructor that initializes the list
    public Container() {
        this.elements = new ArrayList<T>();
    }

    // Method to add an element to the container
    public void addElement(T element) {
        elements.add(element);
    }

    // Method to get all elements in the container that are lighter than the specified element
    public List<T> lighterThan(T element) {
        return elements.stream()
                .filter(i -> i.compareTo(element) < 0)
                .collect(Collectors.toList());
    }

    // Method to get all elements in the container that have a weight between two specified elements
    public List<T> between(T a, T b) {
        return elements.stream()
                .filter(i -> i.compareTo(a) > 0 && i.compareTo(b) < 0)
                .collect(Collectors.toList());
    }

    // Method to compare the sum of weights in this container with another container
    public int compare(Container<? extends Weightable> otherContainer) {
        return Double.compare(this.containerSum(), otherContainer.containerSum());
    }

    // Method to calculate the sum of all weights in the container
    public double containerSum() {
        return elements.stream()
                .mapToDouble(i -> i.getWeight())
                .sum();
    }
}

// Main class to test the Container and Weightable classes
public class ContainerTesting {

    public static void main(String[] args) {

        // Create three containers: two for WeightableDouble and one for WeightableString
        Container<WeightableDouble> container1 = new Container<>();
        Container<WeightableDouble> container2 = new Container<>();

        Container<WeightableString> container3 = new Container<>();

        Scanner scanner = new Scanner(System.in);

        // Read input values for the number of elements in each container
        int n = scanner.nextInt(); // Number of elements in container1
        int m = scanner.nextInt(); // Number of elements in container2
        int p = scanner.nextInt(); // Number of elements in container3

        // Read two double values to create WeightableDouble objects for comparison
        double a = scanner.nextDouble();
        double b = scanner.nextDouble();

        WeightableDouble wa = new WeightableDouble(a); // First comparison value
        WeightableDouble wb = new WeightableDouble(b); // Second comparison value

        // Read and add elements to container1
        for (int i = 0; i < n; i++) {
            double weight = scanner.nextDouble();
            container1.addElement(new WeightableDouble(weight));
        }

        // Read and add elements to container2
        for (int i = 0; i < m; i++) {
            double weight = scanner.nextDouble();
            container2.addElement(new WeightableDouble(weight));
        }

        // Read and add elements to container3
        for (int i = 0; i < p; i++) {
            String s = scanner.next();
            container3.addElement(new WeightableString(s));
        }

        // Get elements from container1 that are lighter than wa
        List<WeightableDouble> resultSmaller = container1.lighterThan(wa);

        // Get elements from container1 that are between wa and wb
        List<WeightableDouble> resultBetween = container1.between(wa, wb);

        // Print elements that are lighter than wa
        System.out.println("Lighter than " + wa.getWeight() + ":");

        for (WeightableDouble wd : resultSmaller) {
            System.out.println(wd.getWeight());
        }

        // Print elements that are between wa and wb
        System.out.println("Between " + wa.getWeight() + " and " + wb.getWeight() + ":");

        for (WeightableDouble wd : resultBetween) {
            System.out.println(wd.getWeight());
        }

        // Compare container1 with container2 and print the result
        System.out.println("Comparison: ");
        System.out.println(container1.compare(container2));

        // Attempt to compare container1 with container3 (this will likely cause an error due to incompatible types)
        System.out.println(container1.compare(container3));
    }
}