package SecondColloquium.ColloquiumAssignments._31AdvancedProgrammingCourse;

import java.util.*;
import java.util.stream.Collectors;

class Student {

    private String studentIndex;
    private String studentName;
    private double midterm1;
    private double midterm2;
    private double labs;

    public Student(String studentIndex, String studentName) {
        this.studentIndex = studentIndex;
        this.studentName = studentName;
        this.midterm1 = 0.0;
        this.midterm2 = 0.0;
        this.labs = 0.0;
    }

    public String getStudentIndex() {
        return studentIndex;
    }

    public String getStudentName() {
        return studentName;
    }

    public void updatePoints(String activity, int points) {
        switch (activity) {
            case "midterm1":
                if (points < 0 || points > 100)
                    throw new IllegalArgumentException();
                midterm1 = points;
                break;
            case "midterm2":
                if (points < 0 || points > 100)
                    throw new IllegalArgumentException();
                midterm2 = points;
                break;
            case "labs":
                if (points < 0 || points > 10)
                    throw new IllegalArgumentException();
                labs = points;
                break;
            default:
                throw new IllegalArgumentException("Invalid Activity!");
        }
    }

    public double getTotalPoints() {
        // midterm1 * 0.45 + midterm2 * 0.45 + labs.
        return midterm1 * 0.45 + midterm2 * 0.45 + labs;
    }

    public int getGrade() {
        double totalPoints = getTotalPoints();

        if (totalPoints < 50) {
            return 5;
        } else if (totalPoints < 60) {
            return 6;
        } else if (totalPoints < 70) {
            return 7;
        } else if (totalPoints < 80) {
            return 8;
        } else if (totalPoints < 90) {
            return 9;
        } else {
            return 10;
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Student student = (Student) object;
        return Objects.equals(studentIndex, student.studentIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentIndex);
    }

    @Override
    public String toString() {
        // ID: 151020 Name: Stefan First midterm: 78 Second midterm 80 Labs: 8 Summary points: 79.10 Grade: 8
        return String.format("ID: %s Name: %s First midterm: %.0f Second midterm %.0f Labs: %.0f Summary points: %.2f Grade: %d",
                getStudentIndex(),
                getStudentName(),
                midterm1,
                midterm2,
                labs,
                getTotalPoints(),
                getGrade());
    }
}

class AdvancedProgrammingCourse {

    private Map<String, Student> studentsMap;

    public AdvancedProgrammingCourse() {
        this.studentsMap = new HashMap<>();
    }

    public void addStudent(Student student) {
        /*
        if (!studentsMap.containsKey(student.getStudentIndex())) {
            studentsMap.put(student.getStudentIndex(), student);
        }
        */

        studentsMap.putIfAbsent(student.getStudentIndex(), student);
    }

    public void updateStudent(String IDNumber, String activity, int points) {
        Student student = studentsMap.get(IDNumber);

        if (student != null) {
            try {
                student.updatePoints(activity, points);
            } catch (IllegalArgumentException ignored) {

            }
        }
    }

    public List<Student> getFirstNStudents(int n) {
        return studentsMap.values().stream()
                .sorted(Comparator.comparingDouble(Student::getTotalPoints)
                        .reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public Map<Integer, Integer> getGradeDistribution() {
        Map<Integer, Integer> gradeDistribution = new TreeMap<>();
        for (int grade = 5; grade <= 10; grade++) {
            gradeDistribution.put(grade, 0);
        }

        studentsMap.values().stream()
                .forEach(student -> {
                    int grade = student.getGrade();
                    gradeDistribution.put(grade, gradeDistribution.get(grade) + 1);
                });

        return gradeDistribution;
    }

    // Count: 1 Min: 79.10 Average: 79.10 Max: 79.10
    public void printStatistics() {
        /* debug
        studentsMap.values().forEach(student ->
                System.out.printf("Debug - %s: %.2f\n", student.getStudentName(), student.getTotalPoints()));
        */

        DoubleSummaryStatistics statistics = studentsMap.values().stream()
                .mapToDouble(Student::getTotalPoints)
                .filter(points -> points >= 50) // all passed students
                .summaryStatistics();

        System.out.printf("Count: %d Min: %.2f Average: %.2f Max: %.2f\n",
                statistics.getCount(),
                statistics.getMin(),
                statistics.getAverage(),
                statistics.getMax());
    }
}

public class AdvancedProgrammingCourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {

        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}