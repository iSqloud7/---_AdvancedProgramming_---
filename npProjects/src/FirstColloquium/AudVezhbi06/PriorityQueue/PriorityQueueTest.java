package FirstColloquium.AudVezhbi06.PriorityQueue;

public class PriorityQueueTest {

    public static void main(String[] args) {

        PriorityQueue<String> priorityQueue = new PriorityQueue<>();

        priorityQueue.add("middle1", 49);
        priorityQueue.add("middle2", 50);
        priorityQueue.add("middle3", 51);
        priorityQueue.add("top", 100);
        priorityQueue.add("bottom", 10);

        String element;
        while ((element = priorityQueue.remove()) != null) { // празнење на редицата
            System.out.println(element);
        }
    }
}
