package SecondColloquium.AudVezhbi02.CountOccurrence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class CountOccurrenceTest {

    public static int count1(Collection<Collection<String>> c, String str) {
        int counter = 0;

        for (Collection<String> collection : c) {
            for (String element : collection) {
                if (element.equalsIgnoreCase(str)) {
                    counter++;
                }
            }
        }

        return counter;
    }

    public static int count2(Collection<Collection<String>> c, String str) {
        // collection of collections -> flatmap
        // Stream<String> value = c.stream().flatMap(collection -> collection.stream())

        return (int) c.stream() // stream of collections
                .flatMap(collection -> collection.stream()) // stream of all string in each collections of strings
                .filter(string -> string.equalsIgnoreCase(str))
                .count();
    }

    public static void main(String[] args) {

        // Test case 1: Single occurrence
        Collection<Collection<String>> collections1 = new ArrayList<>();
        List<String> inner1 = new ArrayList<>();
        inner1.add("apple");
        inner1.add("banana");
        inner1.add("Apple");
        collections1.add(inner1);

        System.out.println("Test case 1 - Expected: 2, count1: " + count1(collections1, "apple"));
        System.out.println("Test case 1 - Expected: 2, count2: " + count2(collections1, "apple"));

        // Test case 2: Multiple occurrences
        Collection<Collection<String>> collections2 = new ArrayList<>();
        List<String> inner2a = new ArrayList<>();
        inner2a.add("apple");
        inner2a.add("banana");
        List<String> inner2b = new ArrayList<>();
        inner2b.add("apple");
        inner2b.add("APPLE");
        collections2.add(inner2a);
        collections2.add(inner2b);

        System.out.println("Test case 2 - Expected: 3, count1: " + count1(collections2, "apple"));
        System.out.println("Test case 2 - Expected: 3, count2: " + count2(collections2, "apple"));

        // Test case 3: No occurrences
        Collection<Collection<String>> collections3 = new ArrayList<>();
        List<String> inner3 = new ArrayList<>();
        inner3.add("banana");
        inner3.add("orange");
        collections3.add(inner3);

        System.out.println("Test case 3 - Expected: 0, count1: " + count1(collections3, "apple"));
        System.out.println("Test case 3 - Expected: 0, count2: " + count2(collections3, "apple"));

        // Test case 4: Mixed case sensitivity
        Collection<Collection<String>> collections4 = new ArrayList<>();
        List<String> inner4a = new ArrayList<>();
        inner4a.add("apple");
        inner4a.add("APPLE");
        inner4a.add("ApPlE");
        collections4.add(inner4a);

        System.out.println("Test case 4 - Expected: 3, count1: " + count1(collections4, "apple"));
        System.out.println("Test case 4 - Expected: 3, count2: " + count2(collections4, "apple"));
    }
}
