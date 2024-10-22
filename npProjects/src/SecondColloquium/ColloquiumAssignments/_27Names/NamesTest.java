package SecondColloquium.ColloquiumAssignments._27Names;

import java.util.*;
import java.util.stream.Collectors;

class Names {

    private Map<String, Integer> namesMap;

    public Names() {
        this.namesMap = new HashMap<>();
    }

    public void addName(String name) {
        namesMap.put(name, namesMap.getOrDefault(name, 0) + 1);
    }

    public void printN(int n) {
        namesMap.entrySet().stream()
                .filter(entry -> entry.getValue() >= n)
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String name = entry.getKey();
                    int count = entry.getValue();
                    int uniqueLetters = getUniqueLetters(name);
                    // Abigail (10) 5
                    System.out.printf("%s (%d) %d\n",
                            name,
                            count,
                            uniqueLetters);
                });
    }

    private int getUniqueLetters(String name) {
        return (int) name.toLowerCase()
                .chars()
                .distinct()
                .count();
    }

    public String findName(int len, int x) {
        List<String> filteredNames = new ArrayList<>();

        namesMap.keySet().stream()
                .filter(name -> name.length() < len)
                .sorted()
                .forEach(filteredNames::add);

        if (filteredNames.isEmpty()) {
            return "";
        }

        int circularIndex = x % filteredNames.size();

        return filteredNames.get(circularIndex);
    }
}

public class NamesTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}