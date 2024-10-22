package SecondColloquium.ColloquiumAssignments._32GenericMap;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface MergeStrategy<T> {

    T merge(T value1, T value2);
}

class MapOps {

    public static <K extends Comparable<K>, V> Map<K, V> merge(Map<K, V> map1, Map<K, V> map2, MergeStrategy<V> strategy) {
        Map<K, V> resultMap = new TreeMap<>(map1);

        for (Map.Entry<K, V> entry : map2.entrySet()) {
            K key = entry.getKey();
            V val2 = entry.getValue();

            if (resultMap.containsKey(key)) {
                V val1 = resultMap.get(key);
                resultMap.put(key, strategy.merge(val1, val2));
            } else {
                resultMap.put(key, val2);
            }
        }

        return resultMap;
    }
}

class SumMergeStrategy implements MergeStrategy<Integer> {

    @Override
    public Integer merge(Integer value1, Integer value2) {
        return value1 + value2;
    }
}

class ConcatMergeStrategy implements MergeStrategy<String> {

    @Override
    public String merge(String value1, String value2) {
        return value1 + value2;
    }
}

class MaxMergeStrategy implements MergeStrategy<Integer> {

    @Override
    public Integer merge(Integer value1, Integer value2) {
        return Math.max(value1, value2);
    }
}

class MaskMergeStrategy implements MergeStrategy<String> {

    @Override
    public String merge(String value1, String value2) {
        if (value2.isEmpty())
            return value1;

        String mask = "*".repeat(value2.length());

        return value1.replace(value2, mask);
    }
}

public class GenericMapTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { //Mergeable integers
            Map<Integer, Integer> mapLeft = new HashMap<>();
            Map<Integer, Integer> mapRight = new HashMap<>();
            readIntMap(sc, mapLeft);
            readIntMap(sc, mapRight);

            //TODO Create an object of type MergeStrategy that will enable merging of two Integer objects into a new Integer object which is their sum
            MergeStrategy<Integer> mergeStrategy = new SumMergeStrategy();

            printMap(MapOps.merge(mapLeft, mapRight, mergeStrategy));
        } else if (testCase == 2) { // Mergeable strings
            Map<String, String> mapLeft = new HashMap<>();
            Map<String, String> mapRight = new HashMap<>();
            readStrMap(sc, mapLeft);
            readStrMap(sc, mapRight);

            //TODO Create an object of type MergeStrategy that will enable merging of two String objects into a new String object which is their concatenation
            MergeStrategy<String> mergeStrategy = new ConcatMergeStrategy();

            printMap(MapOps.merge(mapLeft, mapRight, mergeStrategy));
        } else if (testCase == 3) {
            Map<Integer, Integer> mapLeft = new HashMap<>();
            Map<Integer, Integer> mapRight = new HashMap<>();
            readIntMap(sc, mapLeft);
            readIntMap(sc, mapRight);

            //TODO Create an object of type MergeStrategy that will enable merging of two Integer objects into a new Integer object which will be the max of the two objects
            MergeStrategy<Integer> mergeStrategy = new MaxMergeStrategy();

            printMap(MapOps.merge(mapLeft, mapRight, mergeStrategy));
        } else if (testCase == 4) {
            Map<String, String> mapLeft = new HashMap<>();
            Map<String, String> mapRight = new HashMap<>();
            readStrMap(sc, mapLeft);
            readStrMap(sc, mapRight);

            //TODO Create an object of type MergeStrategy that will enable merging of two String objects into a new String object which will mask the occurrences of the second string in the first string

            MergeStrategy<String> mergeStrategy = new MaskMergeStrategy();
            printMap(MapOps.merge(mapLeft, mapRight, mergeStrategy));
        }
    }

    private static void readIntMap(Scanner sc, Map<Integer, Integer> map) {
        int n = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < n; i++) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            int k = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            map.put(k, v);
        }
    }

    private static void readStrMap(Scanner sc, Map<String, String> map) {
        int n = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < n; i++) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            map.put(parts[0], parts[1]);
        }
    }

    private static void printMap(Map<?, ?> map) {
        map.forEach((k, v) -> System.out.printf("%s -> %s%n", k.toString(), v.toString()));
    }
}