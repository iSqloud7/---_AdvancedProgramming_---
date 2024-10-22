package SecondColloquium.ColloquiumAssignments._26StudentRecords;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Student {

    private String code;
    private String direction;
    private List<Integer> grades;

    public Student(String code, String direction, List<Integer> grades) {
        this.code = code;
        this.direction = direction;
        this.grades = grades;
    }

    // vsgyo4 IKI 8 6 9 7 8 10 7 9 8 9 9 8 8 9 8 8 8 8 9 7 6 7 7 9
    public static Student createStudent(String line) {
        String[] parts = line.split("\\s+");

        String studentCode = parts[0];
        String studentDirection = parts[1];

        List<Integer> studentGrades = new ArrayList<>();
        Arrays.stream(parts)
                .skip(2)
                .mapToInt(Integer::parseInt)
                .forEach(grade -> studentGrades.add(grade));

        return new Student(studentCode, studentDirection, studentGrades);
    }

    public String getCode() {
        return code;
    }

    public String getDirection() {
        return direction;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public double getAverage() {
        return grades.stream()
                .mapToDouble(i -> i.doubleValue())
                .average()
                .orElse(0);
    }

    @Override
    public String toString() {
        return String.format("%s %.2f",
                getCode(), getAverage());
    }
}

class StudentRecords {

    private List<Student> students;

    public StudentRecords() {
        this.students = new ArrayList<>();
    }

    // vsgyo4 IKI 8 6 9 7 8 10 7 9 8 9 9 8 8 9 8 8 8 8 9 7 6 7 7 9
    public int readRecords(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        students = reader.lines()
                .map(Student::createStudent)
                .collect(Collectors.toList());

        return students.size();
    }

    /*
       === WRITING TABLE ===
       IKI
       ookrq3 8.86
       rkmkc7 8.58
       ywiuo5 8.56
       koswj8 8.50
       qbbyt8 8.43
       sawqz8 8.43
    */
    public void writeTable(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);
        Map<String, List<Student>> studentsByDirection = new TreeMap<>();

        students.forEach(student -> {
            studentsByDirection.computeIfAbsent(student.getDirection(), k -> new ArrayList<>()).add(student);
        });

        /*
        for (Student student : students) {
            List<Student> studentList = studentsByDirection.get(student.getDirection());
            if (studentList == null) {
                studentList = new ArrayList<>();
                studentsByDirection.put(student.getDirection(), studentList);
            }

            studentList.add(student);
        }
        */

        studentsByDirection.forEach((direction, studentList) -> {
            writer.println(direction);

            studentList.stream()
                    .sorted((d1, d2) -> {
                        int result = Double.compare(d2.getAverage(), d1.getAverage());
                        if (result == 0) {
                            return d1.getCode().compareTo(d2.getCode());
                        }

                        return result;
                    })
                    .forEach(student -> writer.printf("%s %.2f\n",
                            student.getCode(),
                            student.getAverage()));
        });

        writer.flush();
    }

    public void writeDistribution(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        Map<String, Map<Integer, Integer>> mapMap = new HashMap<>();

        students.forEach(student -> {
            String direction = student.getDirection();
            mapMap.putIfAbsent(direction, new HashMap<>());

            for (int i = 6; i <= 10; i++) {
                mapMap.get(direction).putIfAbsent(i, 0);
            }

            student.getGrades().forEach(grade -> {
                mapMap.get(direction).computeIfPresent(grade, (k, v) -> v + 1);
            });
        });

        mapMap.entrySet().stream()
                .sorted((e1, e2) -> {
                    int compare = Integer.compare(e2.getValue().get(10), e1.getValue().get(10));
                    return compare != 0 ? compare : e1.getKey().compareTo(e2.getKey());
                })
                .forEach(entry -> {
                    String direction = entry.getKey();
                    pw.println(direction);

                    Map<Integer, Integer> gradeMap = entry.getValue();
                    for (int grade = 6; grade <= 10; grade++) {
                        int count = gradeMap.getOrDefault(grade, 0);
                        String stars = "*".repeat((int) Math.ceil(count / 10.0));
                        pw.printf("%2d | %s(%d)\n", grade, stars, count);
                    }
                });

        pw.flush();
    }
}

public class StudentRecordsTest {

    public static void main(String[] args) {

        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}