package SecondColloquium.ColloquiumAssignments._12TextProcessor;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class CosineSimilarityCalculator {

    public static double cosineSimilarity(Collection<Integer> c1, Collection<Integer> c2) {
        int[] array1;
        int[] array2;
        array1 = c1.stream().mapToInt(i -> i).toArray();
        array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1 = 0, down2 = 0;

        for (int i = 0; i < c1.size(); i++) {
            up += (array1[i] * array2[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down1 += (array1[i] * array1[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down2 += (array2[i] * array2[i]);
        }

        return up / (Math.sqrt(down1) * Math.sqrt(down2));
    }
}

class TextPotter {

    public static List<String> getWords(String text) {
        return Arrays.stream(text.split("\\s+"))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    public static String filterText(String str) {
        return str.chars().filter(character -> {
                    if (character >= 'A' && character <= 'Z' ||
                            character >= 'a' && character <= 'z' ||
                            character == ' ') {
                        return true;
                    } else {
                        return false;
                    }
                })
                .mapToObj(character -> (char) character)
                .map(Objects::toString)
                .collect(Collectors.joining(""));
    }
}

class Sentence {

    private Map<String, Integer> wordsMap;
    private String sentence;

    public Sentence(String sentence, Set<String> words) {
        this.wordsMap = new TreeMap<>();
        List<String> myWords = TextPotter.getWords(TextPotter.filterText(sentence));

        for (String myWord : myWords) {
            wordsMap.computeIfPresent(myWord, (k, v) -> ++v);
            wordsMap.putIfAbsent(myWord, 1);
        }

        for (String word : words) {
            wordsMap.putIfAbsent(word, 0);
        }

        this.sentence = sentence;
    }

    public List<Integer> getVector() {
        return wordsMap.values().stream()
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getWordsMap() {
        return wordsMap;
    }

    @Override
    public String toString() {
        return sentence;
    }
}

class TextProcessor {

    private List<Sentence> sentences;

    public TextProcessor() {
        this.sentences = new ArrayList<>();
    }

    /* Format:
       Napredno programiranje..
       Napredno programiranje
    */
    public void readText(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        List<String> inputs = reader.lines().collect(Collectors.toList());
        Set<String> wordsSet = new TreeSet<>();

        for (String input : inputs) {
            List<String> words = TextPotter.getWords(TextPotter.filterText(input));
            wordsSet.addAll(words);
        }

        for (String input : inputs) {
            Sentence sentence = new Sentence(input, wordsSet);
            sentences.add(sentence);
        }
    }

    public void printTextsVectors(OutputStream os) {
        PrintWriter writer = new PrintWriter(os);

        sentences.forEach(sentence -> writer.println(sentence.getVector()));

        writer.flush();
    }

    public void printCorpus(OutputStream os, int n, boolean ascending) {
        PrintWriter pw = new PrintWriter(os);

        Map<String, Integer> map = sentences.stream()
                .map(sentence -> sentence.getWordsMap().entrySet())
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.summingInt(Map.Entry::getValue)
                ));
        Comparator<Map.Entry<String, Integer>> comparator =
                Comparator.<Map.Entry<String, Integer>, Integer>comparing(Map.Entry::getValue, ascending ? Comparator.naturalOrder() : Comparator.reverseOrder())
                        .thenComparing(Map.Entry::getKey);

        map.entrySet().stream()
                .sorted(comparator)
                .limit(n)
                .forEach(entry -> pw.println(String.format("%s : %d", entry.getKey(), entry.getValue())));

        pw.flush();
    }

    public void mostSimilarTexts(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);

        pw.print(mostSimilar());

        pw.flush();
    }

    public String mostSimilar() {
        Sentence first = sentences.get(0);
        Sentence second = sentences.get(1);
        double min = Math.abs(1 - CosineSimilarityCalculator.cosineSimilarity(first.getVector(), second.getVector()));
        for (int i = 1; i < sentences.size(); i++) {
            for (int i1 = 0; i1 < sentences.size(); i1++) {
                if (!sentences.get(i).equals(sentences.get(i1))) {
                    if (Math.abs(1 - CosineSimilarityCalculator.cosineSimilarity(sentences.get(i).getVector(), sentences.get(i1).getVector())) < min) {
                        min = Math.abs(1 - CosineSimilarityCalculator.cosineSimilarity(sentences.get(i).getVector(), sentences.get(i1).getVector()));
                        first = sentences.get(i);
                        second = sentences.get(i1);
                    }
                }
            }
        }
        List<Sentence> list = new ArrayList<>();
        list.add(first);
        list.add(second);
        String s = String.format("%s\n%s\n%.10f", first, second, CosineSimilarityCalculator.cosineSimilarity(first.getVector(), second.getVector()));
        return s;
    }
}

public class TextProcessorTest {

    public static void main(String[] args) {

        TextProcessor textProcessor = new TextProcessor();

        textProcessor.readText(System.in);

        System.out.println("===PRINT VECTORS===");
        textProcessor.printTextsVectors(System.out);

        System.out.println("PRINT FIRST 20 WORDS SORTED ASCENDING BY FREQUENCY ");
        textProcessor.printCorpus(System.out, 20, true);

        System.out.println("PRINT FIRST 20 WORDS SORTED DESCENDING BY FREQUENCY");
        textProcessor.printCorpus(System.out, 20, false);

        System.out.println("===MOST SIMILAR TEXTS===");
        textProcessor.mostSimilarTexts(System.out);
    }
}