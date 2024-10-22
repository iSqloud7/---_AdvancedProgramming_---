package FirstColloquium.ColloquiumAssignments.Container;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Container<T extends Weightable> { // тип кој наследува од Weightable interface

    private List<T> elements;

    public Container() {
        this.elements = new ArrayList<T>();
    }

    public void addElement(T element) {
        elements.add(element);
    }

    public List<T> lighterThan(T element) {
        return elements.stream()
                .filter(i -> i.compareTo(element) < 0)
                .collect(Collectors.toList());
    }

    public List<T> between(T a, T b) {
        return elements.stream()
                .filter(i -> i.compareTo(a) > 0 && i.compareTo(b) < 0)
                .collect(Collectors.toList());
    }

    public int compare(Container<? extends Weightable> otherContainer) {
        return Double.compare(this.containerSum(), otherContainer.containerSum());
    }

    public double containerSum() {
        return elements.stream()
                .mapToDouble(i -> i.getWeight())
                .sum();
    }
}
