package FirstColloquium.AudVezhbi05.ArrayList;

import java.util.ArrayList;
import java.util.List;

public class ArrayListTest {

    public static void main(String[] args) {

        List<Integer> integerList = new ArrayList<>(100);
        List<String> stringList = new ArrayList<>();

        integerList.add(7);
        integerList.add(3);
        integerList.add(7);
        integerList.add(7);
        integerList.add(7);

        stringList.add("Ivan");

        System.out.println(integerList); // [7, 7, 7, 7, 7]
        System.out.println(stringList); // [Ivan]

        System.out.println("Size: " + integerList.size()); // Size: 5
        System.out.println("Index: " + integerList.get(3)); // Index: 7
        System.out.println("Contains: " + integerList.contains(7)); // Contains: true
        System.out.println("Contains: " + integerList.contains(5)); // Contains: false
        System.out.println("IndexOf: " + integerList.indexOf(7)); // IndexOf: 0
        System.out.println("LastIndexOf: " + integerList.lastIndexOf(7)); // LastIndexOf: 4

        // предикат -> функциски интерфејс
        // - зема 1 аргумент
        // - соодветно враќа true/false
        System.out.println("RemoveIf: " + integerList.removeIf(i -> i > 5)); // RemoveIf: true
        System.out.println("After RemoveIf: " + integerList);

        // со streams, мора да има на крај terminal action
        /*
        integerList.stream()
                .filter()
                .forEach();
        */
    }
}
