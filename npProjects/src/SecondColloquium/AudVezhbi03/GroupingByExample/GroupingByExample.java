package SecondColloquium.AudVezhbi03.GroupingByExample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

class Person {

    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person(String line) {
        String[] parts = line.split("\\s+");

        this.name = parts[0];
        this.age = Integer.parseInt(parts[1]);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

public class GroupingByExample {

    /* Input:
Ivan 21
Marija 22
Ivan 20
Ilija 20
Ilija 20
Ilija 30
Marija 39
Ivan 11
Ivan 11
Ivan 32
    */

    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        List<Person> people = reader.lines()
                .map(line -> new Person(line))
                .collect(Collectors.toList());

        System.out.println(people);

        System.out.println("\n===== CALCULATE FREQUENCY OF A GIVEN NAME =====");
        Map<String, Integer> frequencyByName = new HashMap<>();

        for (Person person : people) {
            frequencyByName.putIfAbsent(person.getName(), 0); // if name of person doesn't exist in map, frequency == 0
            // frequencyByName.put(person.getName(), frequencyByName.get(person.getName() + 1)); // old way -> computeIfPresent()
            frequencyByName.computeIfPresent(person.getName(), (k, v) -> {
                return ++v;
            });
        }

        System.out.println(frequencyByName);

        System.out.println("\n===== CALCULATE FREQUENCY OF A GIVEN NAME WITH GROUPING BY =====");
        Map<String, Double> frequencyByNameWithGroupingBy = people.stream()
                .collect(Collectors.groupingBy(
                        person -> person.getName(), // key
                        TreeMap::new, // sorted by key
                        // Collectors.counting() // value
                        Collectors.averagingInt(person -> person.getAge()) // value
                ));

        System.out.println(frequencyByNameWithGroupingBy);

        Map<String, List<Person>> allObjectsWithTheSameName = people.stream()
                .collect(Collectors.groupingBy(
                        person -> person.getName(),
                        TreeMap::new,
                        Collectors.toList()
                ));

        System.out.println(allObjectsWithTheSameName);
    }
}
