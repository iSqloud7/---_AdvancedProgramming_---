package FirstColloquium.AudVezhbi05.OldestPerson;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class OldestPersonTest {

    public static List<Person> readData(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        return reader.lines() // враќа stream од strings
                .map(line -> new Person(line)) // од една линија да се направи нешто
                .collect(Collectors.toList()); // Collector е интерфејс кој овозможува креирање на колекции
    }

    public static void main(String[] args) {

        File file = new File("C:\\Users\\Ivan\\Desktop\\NP_AudVezhbi\\AudVezhbiNP\\src\\FirstColloquium\\AudVezhbi05\\OldestPerson\\Input.txt");

        try {
            List<Person> people = readData(new FileInputStream(file));

            // FirstWay
            System.out.println("FirstWay");
            System.out.println("==============================");
            Collections.sort(people);
            System.out.println(people.get(people.size() - 1));

            // SecondWay without Optional
            System.out.println("SecondWay");
            System.out.println("==============================");
            System.out.println(people.stream()
                    .max(Comparator.naturalOrder())
                    .get());

            // ThirdWay with Optional
            System.out.println("ThirdWay");
            System.out.println("==============================");
            if (people.stream().max(Comparator.naturalOrder()).isPresent()) {
                System.out.println(people.stream()
                        .max(Comparator.naturalOrder())
                        .get());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
