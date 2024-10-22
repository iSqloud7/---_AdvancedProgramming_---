package LaboratoryExercises_NP.Lab_VII.TermFrequency;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

class TermFrequency {

    private final Map<String, Integer> map;
    private int count;

    public TermFrequency(InputStream in, String[] stopWords) {
        Scanner scanner = new Scanner(in);
        this.map = new HashMap<>();
        this.count = 0;

        while (scanner.hasNext()) {
            String word = scanner.next();
            word = word
                    .toLowerCase()
                    .replaceAll("[,.-]", "")
                    .trim();

            if (!Arrays.asList(stopWords).contains(word) && !word.isEmpty()) {
                map.computeIfPresent(word, (k, v) -> ++v);
                map.putIfAbsent(word, 1);

                count++;
            }
        }
    }

    public int countTotal() {
        return count;
    }

    public int countDistinct() {
        return map.size();
    }

    public List<String> mostOften(int k) {
        return map.keySet().stream().sorted(Comparator.comparing(map::get).reversed().thenComparing(Object::toString)).limit(k).collect(Collectors.toList());
    }
}

class TermFrequencyTest {

    public static void main(String[] args) throws FileNotFoundException {

        String[] stop = new String[]{"во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја"};
        TermFrequency tf = new TermFrequency(System.in, stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}