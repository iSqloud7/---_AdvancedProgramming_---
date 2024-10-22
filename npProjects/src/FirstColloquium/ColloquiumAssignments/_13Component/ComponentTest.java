package FirstColloquium.ColloquiumAssignments._13Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class InvalidPositionException extends Exception {

    public InvalidPositionException(String message) {
        super(message);
    }
}

// FIREFOX 1 RED 30 3
class Component implements Comparable<Component> {

    private String color;
    private int weight;
    private List<Component> components;
    private int position;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        this.components = new ArrayList<>();
        this.position = -1;
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    public int getPosition() {
        return position;
    }

    public Component setPosition(int position) {
        this.position = position;
        return this;
    }

    private void sorting() {
        components = components.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public void addComponent(Component component) {
        components.add(component);

        sorting();
    }

    @Override
    public int compareTo(Component other) {
        if (this.weight == other.weight) {
            if (this.color.compareTo(other.color) == 0) {
                return Integer.compare(other.components.stream()
                                .mapToInt(component -> component.weight)
                                .sum(),
                        this.components.stream()
                                .mapToInt(component -> component.weight)
                                .sum());
            }

            return this.color.compareTo(other.color);
        }

        return Integer.compare(this.weight, other.weight);
    }

    public void changeColor(int weight, String color) {
        if (this.weight < weight) {
            this.color = color;
        }

        if (!components.isEmpty()) {
            components.forEach(component -> component.changeColor(weight, color));
        }

        sorting();
    }

    /*
       === ORIGINAL WINDOW ===
       WINDOW FIREFOX
       1:30:RED
       ---40:GREEN
       ------50:BLUE
       ---------60:CYAN
       ------50:RED
       ---90:MAGENTA
       2:80:YELLOW
       ---35:WHITE
    */
    public String getComponentStringRepresentation(int intend) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < intend; i++) {
            builder.append("---");
        }

        builder.append(String.format("%d:%s\n",
                getWeight(), getColor()));

        components.forEach(component -> builder.append(component.getComponentStringRepresentation(intend + 1)));

        return builder.toString();
    }
}

class Window {

    private String name;
    private List<Component> components;

    public Window(String name) {
        this.name = name;
        this.components = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    private void sorting() {
        components = components.stream()
                .sorted(Comparator.comparingInt(Component::getPosition))
                .collect(Collectors.toList());
    }

    public void addComponent(int position, Component component) throws InvalidPositionException {
        if (components.isEmpty()) {
            components.add(component.setPosition(position));
            return;
        }

        for (Component comp : components) {
            if (comp.getPosition() == position) {
                throw new InvalidPositionException(String.format("Invalid position %d, alredy taken!", position));
            }
        }

        components.add(component.setPosition(position));
        sorting();
    }

    public void changeColor(int weight, String color) {
        for (Component component : components) {
            component.changeColor(weight, color);
        }

        sorting();
    }

    public void swichComponents(int pos1, int pos2) {
        int tmp1 = 0;
        int tmp2 = 0;
        Component component1 = null;
        Component component2 = null;

        for (Component component : components) {
            if (component.getPosition() == pos1) {
                tmp1 = pos1;
                component1 = component;
            } else if (component.getPosition() == pos2) {
                tmp2 = pos2;
                component2 = component;
            }
        }

        int tmp = tmp1;
        component1.setPosition(tmp2);
        component2.setPosition(tmp);

        sorting();
    }

    /*
       === ORIGINAL WINDOW ===
       WINDOW FIREFOX
       1:30:RED
       ---40:GREEN
       ------50:BLUE
       ---------60:CYAN
       ------50:RED
       ---90:MAGENTA
       2:80:YELLOW
       ---35:WHITE
    */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("WINDOW ")
                .append(getName())
                .append("\n");

        components.forEach(component -> {
            builder.append(component.getPosition())
                    .append(":")
                    .append(component.getComponentStringRepresentation(0));
        });

        return builder.toString();
    }
}

public class ComponentTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if (what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}