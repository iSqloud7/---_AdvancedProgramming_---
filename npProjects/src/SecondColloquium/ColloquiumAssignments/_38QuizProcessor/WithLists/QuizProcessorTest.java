package SecondColloquium.ColloquiumAssignments._38QuizProcessor.WithLists;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class QuizEntryCanNotBeProcessedException extends Exception {

    public QuizEntryCanNotBeProcessedException() {
        super("A quiz must have same number of correct and selected answers");
    }
}

class Quiz {

    private final String studentID;
    private final List<String> correctAnswers;
    private final List<String> studentAnswers;
    private double studentPoints;

    public static Quiz createQuiz(String line) throws QuizEntryCanNotBeProcessedException {
        String[] parts = line.split(";");

        // String studentID = parts[0];
        List<String> correctAnswers = new ArrayList<>(Arrays.asList(parts[1].split(",")));
        List<String> studentAnswers = new ArrayList<>(Arrays.asList(parts[2].split(",")));

        return new Quiz(parts[0], correctAnswers, studentAnswers);
    }

    public Quiz(String studentID, List<String> correctAnswers, List<String> studentAnswers) throws QuizEntryCanNotBeProcessedException {

        if (correctAnswers.size() != studentAnswers.size()) {
            throw new QuizEntryCanNotBeProcessedException();
        }
        this.studentID = studentID;
        this.correctAnswers = correctAnswers;
        this.studentAnswers = studentAnswers;

        calculateStudentPoints(correctAnswers, studentAnswers);
    }

    private void calculateStudentPoints(List<String> correctAnswers, List<String> studentAnswers) {
        IntStream.range(0, correctAnswers.size())
                .forEach(i -> {
                    if (correctAnswers.get(i).equals(studentAnswers.get(i))) {
                        studentPoints += 1;
                    } else {
                        studentPoints -= 0.25;
                    }
                });
    }

    public String getStudentID() {
        return studentID;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public List<String> getStudentAnswers() {
        return studentAnswers;
    }

    public double getStudentPoints() {
        return studentPoints;
    }
}

class QuizProcessor {

    // 200000;C, D, D, D, A, C, B, D, D;C, D, D, D, D, B, C, D, A
    public static Map<String, Double> processAnswers(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        return reader.lines()
                .map(line -> {
                    try {
                        return Quiz.createQuiz(line);
                    } catch (QuizEntryCanNotBeProcessedException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Quiz::getStudentID, Quiz::getStudentPoints, Double::sum, TreeMap::new));
    }
}

public class QuizProcessorTest {

    public static void main(String[] args) {

        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}