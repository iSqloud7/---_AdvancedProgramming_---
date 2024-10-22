package FirstColloquium.AudVezhbi05.CourseGrades;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Course {

    private List<Student> students;

    public Course() {
        this.students = new ArrayList<>(); // може и LinkedList()
    }

    public void readData(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        this.students = reader.lines()
                .map(line -> Student.createStudent(line)) // мапирањето не е преку конструктор, туку преку метод
                .collect(Collectors.toList());
    }

    public void printSortedData(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);

        this.students.stream()
                .sorted() // растечки редослед ако е во compare(this.grade, other.grade)
                .forEach(student -> writer.println(student));
        // со println ќе се печати [surname name grade]

        writer.flush();
        // writer.close(); // внатре прави и flush()
    }

    public void printDetailedData(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);

        this.students.stream()
                .sorted()
                .forEach(student -> writer.println(student.printFullInformation()));

        writer.flush();
    }

    public void printDistribution(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);

        int gradeDistribution[] = new int[6];

        for (Student student : students) {
            gradeDistribution[student.getGrade() - 'A']++;
        }

        for (int i = 0; i < 6; i++) {
            writer.printf("%c -> %d\n", i + 'A', gradeDistribution[i]);
        }

        writer.flush();
    }
}
