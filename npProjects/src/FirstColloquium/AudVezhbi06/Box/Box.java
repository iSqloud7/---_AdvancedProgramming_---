package FirstColloquium.AudVezhbi06.Box;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Box<T extends Drawable> { // ограничување со интерфејсот

    private List<T> elements;
    public static Random random = new Random();

    public Box() {
        this.elements = new ArrayList<>();
    }

    public void addElement(T element) {
        elements.add(element);
    }

    public boolean isEmpty() {
        return elements.isEmpty(); // or elements.size() > 0;
    }

    public T drawItem() {
        if (isEmpty()) {
            return null;
        }

        /*
        int index = random.nextInt(elements.size());
        T elem = elements.get(index);
        elements.remove(elem);

        return elem;
        */

        return elements.remove(random.nextInt(elements.size()));
    }
}