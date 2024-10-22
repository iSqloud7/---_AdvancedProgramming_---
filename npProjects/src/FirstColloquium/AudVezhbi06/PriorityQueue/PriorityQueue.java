package FirstColloquium.AudVezhbi06.PriorityQueue;

import java.util.ArrayList;
import java.util.List;

public class PriorityQueue<T> {

    // item, priority, за чување на две својства во едно (објект) -> се користи класа
    private List<PriorityQueueElement<T>> elements;

    public PriorityQueue() {
        this.elements = new ArrayList<>();
    }

    public void add(T item, int priority) {
        PriorityQueueElement<T> newElement = new PriorityQueueElement<>(item, priority);
        // додавањето на елементите да биде според приоритет

        int i;
        for (i = 0; i < elements.size(); i++) {
            if (newElement.compareTo(elements.get(i)) <= 0) {
                break;
            }
        }

        // elements.add(newElement); // додава на крај во листата
        elements.add(i, newElement); // додава на дадена специфична позиција
    }

    public T remove() {
        if (elements.size() == 0) {
            return null;
        }

        return elements.remove(elements.size() - 1).getElement(); // .getElement(), треба да се врати PriorityQueueElement
    }
}
