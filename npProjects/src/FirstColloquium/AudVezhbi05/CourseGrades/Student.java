package FirstColloquium.AudVezhbi05.CourseGrades;

public class Student implements Comparable<Student> {

    private String surname;
    private String name;
    private int exam1;
    private int exam2;
    private int exam3;
    private char grade;

    public Student(String surname, String name, int exam1, int exam2, int exam3) {
        this.surname = surname;
        this.name = name;
        this.exam1 = exam1;
        this.exam2 = exam2;
        this.exam3 = exam3;
        setGrade();
    }

    public static Student createStudent(String line) { // наместо преку конструктор, преку статички метод се креира студент
        String[] parts = line.split(":");
        return new Student(
                parts[0], // surname
                parts[1], // name
                Integer.parseInt(parts[2]), // exam1
                Integer.parseInt(parts[3]), // exam2
                Integer.parseInt(parts[4])); // exam3
    }

    public double totalPoints() {
        return 0.25 * exam1 + 0.30 * exam2 + 0.45 * exam3;
    }

    public char getGrade() {
        return grade;
    }

    public void setGrade() {
        double points = totalPoints();

        if (points >= 90) {
            this.grade = 'A';
        } else if (points >= 80) {
            this.grade = 'B';
        } else if (points >= 70) {
            this.grade = 'C';
        } else if (points >= 60) {
            this.grade = 'D';
        } else if (points >= 50) {
            this.grade = 'E';
        } else {
            this.grade = 'F';
        }
    }

    // Формат за печатење на екран (обичен)
    @Override
    public String toString() {
        return String.format("%s %s %s\n", surname, name, getGrade());
    }

    // Формат за печатење на целиот формат
    public String printFullInformation() {
        return String.format("%s %s %d %d %d %.2f %c\n",
                surname, name, exam1, exam2, exam3, totalPoints(), getGrade());
    }

    @Override
    public int compareTo(Student other) {
        return Character.compare(this.grade, other.grade);
    }
}