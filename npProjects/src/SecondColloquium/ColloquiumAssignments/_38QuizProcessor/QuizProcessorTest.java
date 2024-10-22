package SecondColloquium.ColloquiumAssignments._38QuizProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

class Student implements Comparable<Student> {

    private String studentID;
    private double score;

    public Student(String studentID, double score) {
        this.studentID = studentID;
        this.score = score;
    }

    public String getStudentID() {
        return studentID;
    }

    public double getScore() {
        return score;
    }

    public double calculateScore(String[] correctAnswers, String[] studentAnswers) {
        double score = 0.0;

        for (int i = 0; i < correctAnswers.length; i++) {
            if (studentAnswers[i].equals(correctAnswers[i])) {
                score += 1.0;
            } else {
                score -= 0.25;
            }
        }

        return score;
    }

    @Override
    public String toString() {
        // 151020 -> 1.75
        return String.format("%s -> %.2f\n", getStudentID(), getScore());
    }

    @Override
    public int compareTo(Student other) {
        return Double.compare(other.score, this.score);
    }
}

class QuizProcessor {

    // ID; C1, C2, C3, C4, C5, … ,Cn; A1, A2, A3, A4, A5, …,An.
    // каде што ID е индексот на студентот, Ci е точниот одговор на i-то прашање, а Ai е одговорот на студентот на i-то прашање.
    public static Map<String, Double> processAnswers(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Map<String, Double> studentScores = new TreeMap<>();

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length != 3)
                    continue;

                String studentID = parts[0];
                String[] correctAnswers = parts[1].split(",");
                String[] studentAnswers = parts[2].split(",");

                if (correctAnswers.length != studentAnswers.length) {
                    System.out.println("A quiz must have same number of correct and selected answers");
                    continue;
                }

                Student student = new Student(studentID, 0.0);
                double score = student.calculateScore(correctAnswers, studentAnswers);

                studentScores.put(studentID, score);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return studentScores;
    }
}

public class QuizProcessorTest {

    public static void main(String[] args) {

        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}