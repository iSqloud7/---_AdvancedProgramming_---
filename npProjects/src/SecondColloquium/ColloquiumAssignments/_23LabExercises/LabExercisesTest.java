package SecondColloquium.ColloquiumAssignments._23LabExercises;

import java.util.*;
import java.util.stream.Collectors;

class Student {

    private String studentIndex;
    private List<Integer> points;

    Student(String studentIndex, List<Integer> points) {
        this.studentIndex = studentIndex;
        this.points = points;
    }

    public String getStudentIndex() {
        return studentIndex;
    }

    public List<Integer> getPoints() {
        return points;
    }

    public double getTotalPoints() {
        return points.stream()
                .mapToDouble(i -> i.doubleValue())
                .sum();
    }

    public double getAveragePoints() {
        return getTotalPoints() / 10.0; // max 10 labs
    }

    public boolean hasFailed() {
        return points.size() < 8;
    }

    public int getYearOfStudy() {
        int year = Integer.parseInt(studentIndex.substring(0, 2));

        return 20 - year;
    }

    @Override
    public String toString() {
        // 122026 YES 0.00
        String passed = hasFailed() ? "NO" : "YES";
        return String.format("%s %s %.2f",
                getStudentIndex(),
                passed,
                getAveragePoints());
    }
}

class LabExercises {

    private List<Student> students;

    public LabExercises() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void printByAveragePoints(boolean ascending, int n) {
        List<Student> sortedStudents;
        if (ascending) {
            sortedStudents = students.stream()
                    .sorted((s1, s2) -> {
                        int result = Double.compare(s1.getTotalPoints(), s2.getTotalPoints());
                        if (result == 0) {
                            return s1.getStudentIndex().compareTo(s2.getStudentIndex());
                        }

                        return result;
                    })
                    .limit(n)
                    .collect(Collectors.toList());
        } else { // descending
            sortedStudents = students.stream()
                    .sorted((s1, s2) -> {
                        int result = Double.compare(s2.getTotalPoints(), s1.getTotalPoints());
                        if (result == 0) {
                            return s2.getStudentIndex().compareTo(s1.getStudentIndex());
                        }

                        return result;
                    })
                    .limit(n)
                    .collect(Collectors.toList());
        }

        sortedStudents.forEach(student -> System.out.println(student));
    }

    public List<Student> failedStudents() {
        return students.stream()
                .filter(student -> student.hasFailed())
                .sorted((s1, s2) -> {
                    int result = s1.getStudentIndex().compareTo(s2.getStudentIndex());
                    if (result == 0) {
                        return Double.compare(s1.getTotalPoints(), s2.getTotalPoints());
                    }

                    return result;
                })
                .collect(Collectors.toList());
    }

    public Map<Integer, Double> getStatisticsByYear() {
        return students.stream()
                .filter(student -> !student.hasFailed())
                .collect(Collectors.groupingBy(student1 -> student1.getYearOfStudy(),
                        Collectors.averagingDouble(Student::getAveragePoints)));

        /*
        Map<Integer, Double> averagePointsByYear = new TreeMap<>();
        for (Map.Entry<Integer, List<Student>> entry : studentByYearOfStudy.entrySet()) {
            int yearOfStudy = entry.getKey();
            List<Student> studentsOfYear = entry.getValue();
            double totalPoints = 0.0;
            for (Student student : studentsOfYear) {
                totalPoints += student.getAveragePoints();
            }
            double averagePoints = totalPoints / studentsOfYear.size();
            averagePointsByYear.put(yearOfStudy, averagePoints);
        }
        return averagePointsByYear;
        */
    }
}

public class LabExercisesTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}