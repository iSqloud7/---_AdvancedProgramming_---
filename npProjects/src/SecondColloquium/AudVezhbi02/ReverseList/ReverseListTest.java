package SecondColloquium.AudVezhbi02.ReverseList;

import java.util.*;

public class ReverseListTest {

    public static <T> void reversePrint1(Collection<T> collection) {
        // Collection е интерфејс на највисоко ниво

        List<T> list = new ArrayList<>();
        list.addAll(collection);

        for (int i = list.size() - 1; i >= 0; i--) {
            System.out.println("\t*" + list.get(i));
        }
    }

    public static <T> void reversePrint2(Collection<T> collection) {
        List<T> list = new ArrayList<>(collection);
        Collections.reverse(list);

        list.stream()
                .forEach(x -> System.out.println("\t*" + x));
    }

    public static void main(String[] args) {

        Collection<Integer> collectionOfIntegers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println("Normal list: " + collectionOfIntegers + "\n");

        reversePrint1(collectionOfIntegers);
        System.out.println("\n[]=======[]\n");
        reversePrint2(collectionOfIntegers);
    }
}
