package SecondColloquium.AudVezhbi03.Names;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Name implements Comparable<Name> {

    private String name;
    private int frequency;

    public Name(String name, int frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public int compareTo(Name other) {
        return Integer.compare(this.getFrequency(), other.getFrequency());
    }

    @Override
    public String toString() {
        return "Name{" +
                "name='" + name + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}

public class NamesTest {

    private static Map<String, Integer> readNamesWithCollectorGroupingBy(InputStream inputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        return reader.lines()
                .map(line -> {
                    String[] parts = line.split("\\s+");

                    String name = parts[0];
                    int frequency = Integer.parseInt(parts[1]);

                    return new Name(name, frequency);
                }).collect(Collectors.groupingBy(
                        name -> name.getName(), // with a function to extract the key
                        Collectors.summingInt(Name::getFrequency)
                ));
    }

    private static Map<String, Integer> readNamesWithCollectorToMap(InputStream inputStream) { // Second way
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        /* into a list
        List<Name> names = reader.lines()
                .map(line -> {
                    String[] parts = line.split("\\s+");

                    String name = parts[0];
                    int frequency = Integer.parseInt(parts[1]);

                    return new Name(name, frequency);
                })
                .collect(Collectors.toList());
        */

        // into a map with toMap()
        return reader.lines()
                .map(line -> {
                    String[] parts = line.split("\\s+");

                    String name = parts[0];
                    int frequency = Integer.parseInt(parts[1]);

                    return new Name(name, frequency);
                })
                .collect(Collectors.toMap(
                        nameObject -> nameObject.getName(), // key
                        nameObject -> nameObject.getFrequency() // value
                ));
    }

    // key -> name (boy/girl)
    // value -> frequency
    private static Map<String, Integer> readNames(InputStream inputStream) { // First way
        Map<String, Integer> frequencyByName = new HashMap<>();
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");

            String name = parts[0];
            int frequency = Integer.parseInt(parts[1]);

            frequencyByName.put(name, frequency);
        }

        return frequencyByName;
    }

    public static void main(String[] args) {

        try {
            // First way
            // Map<String, Integer> boyNamesMap = readNames(new FileInputStream("C:\\Users\\Ivan\\Desktop\\NP_AudVezhbi\\AudVezhbiNP\\src\\SecondColloquium\\AudVezhbi03\\Names\\boynames.txt"));
            // Map<String, Integer> girlNamesMap = readNames(new FileInputStream("C:\\Users\\Ivan\\Desktop\\NP_AudVezhbi\\AudVezhbiNP\\src\\SecondColloquium\\AudVezhbi03\\Names\\girlnames.txt"));

            // Second way
            // Map<String, Integer> boyNamesMap = readNamesWithCollectorToMap(new FileInputStream("C:\\Users\\Ivan\\Desktop\\NP_AudVezhbi\\AudVezhbiNP\\src\\SecondColloquium\\AudVezhbi03\\Names\\boynames.txt"));
            // Map<String, Integer> girlNamesMap = readNamesWithCollectorToMap(new FileInputStream("C:\\Users\\Ivan\\Desktop\\NP_AudVezhbi\\AudVezhbiNP\\src\\SecondColloquium\\AudVezhbi03\\Names\\girlnames.txt"));

            // Third way
            Map<String, Integer> boyNamesMap = readNamesWithCollectorGroupingBy(new FileInputStream("C:\\Users\\Ivan\\Desktop\\NP_AudVezhbi\\AudVezhbiNP\\src\\SecondColloquium\\AudVezhbi03\\Names\\boynames.txt"));
            Map<String, Integer> girlNamesMap = readNamesWithCollectorGroupingBy(new FileInputStream("C:\\Users\\Ivan\\Desktop\\NP_AudVezhbi\\AudVezhbiNP\\src\\SecondColloquium\\AudVezhbi03\\Names\\girlnames.txt"));

            // System.out.println(boyNamesMap);
            // System.out.println(girlNamesMap);

            /* frequency of only male -> female names
            // keySet() -> all keys
            for (String boyName : boyNamesMap.keySet()) {
                if (girlNamesMap.containsKey(boyName)) {
                    System.out.println(String.format("[Name: %s: Frequency: %d]",
                            boyName, boyNamesMap.get(boyName) + girlNamesMap.get(boyName)));
                }
            }
            */

            System.out.println("\n===== SET OF ALL BOY-GIRL NAMES AND THEIR FREQUENCY =====");
            Set<String> allNames = new HashSet<>();

            allNames.addAll(boyNamesMap.keySet());
            allNames.addAll(girlNamesMap.keySet());

            allNames.stream()
                    .filter(name -> boyNamesMap.containsKey(name) &&
                            girlNamesMap.containsKey(name))
                    .map(name -> new Name(name, boyNamesMap.get(name) + girlNamesMap.get(name)))
                    .sorted(Comparator.reverseOrder())
                    .forEach(name -> System.out.println(name)); // toString()
                    /*
                    .forEach(name -> System.out.println(String.format("[Name: %s: Frequency: %d]",
                            name, boyNamesMap.get(name) + girlNamesMap.get(name))));
                    */

            System.out.println("\n===== UNISEX NAMES =====");
            Map<String, Integer> unisexNames = new HashMap<>();

            allNames.stream()
                    .filter(name -> boyNamesMap.containsKey(name) &&
                            girlNamesMap.containsKey(name))
                    .forEach(name -> unisexNames.put(name, boyNamesMap.get(name) + girlNamesMap.get(name)));

            System.out.println(unisexNames);

            System.out.println("\n===== SORTING MAP BY VALUE IN DESCENDING ORDER =====");
            // sort map by value in descending order
            // entrySet() -> returns a set of all key-value pairs
            unisexNames.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // set of map entries, not a map
                    .forEach(entry -> System.out.println(String.format("%s:%d",
                            entry.getKey(),
                            entry.getValue())));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}