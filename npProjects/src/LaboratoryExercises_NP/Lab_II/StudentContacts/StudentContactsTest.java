package LaboratoryExercises_NP.Lab_II.StudentContacts;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

enum Operator {

    VIP,
    ONE,
    TMOBILE
}

class Student {
    private final String firstName;
    private final String lastName;
    private final String city;
    private final int age;
    private final long index;
    private Contact[] contacts;
    private int n;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        this.contacts = new Contact[0];
        this.n = 0;
    }

    @Override
    public String toString() {
        return "{\"ime\":\"" +
                firstName +
                "\", \"prezime\":\"" +
                lastName +
                "\", \"vozrast\":" +
                age +
                ", \"grad\":\"" +
                city +
                "\", \"indeks\":" +
                index +
                ", \"telefonskiKontakti\":" +
                Arrays.toString(getPhoneContacts()) +
                ", \"emailKontakti\":" +
                Arrays.toString(getEmailContacts()) +
                "}";
    }

    public void addEmailContact(String date, String email) {
        contacts = Arrays.copyOf(contacts, n + 1);
        contacts[n] = new EmailContact(date, email);
        n++;
    }

    public void addPhoneContact(String date, String phone) {
        contacts = Arrays.copyOf(contacts, n + 1);
        contacts[n] = new PhoneContact(date, phone);
        n++;
    }

    public Contact[] getEmailContacts() {
        Contact[] array;
        int i = 0;

        for (Contact c : contacts) {
            if (c.getType().equals("Email")) {
                i++;
            }
        }

        array = new Contact[i];
        i = 0;

        for (Contact c : contacts) {
            if (c.getType().equals("Email")) {
                array[i] = c;
                i++;
            }
        }

        return array;
    }

    public Contact[] getPhoneContacts() {
        Contact[] array;
        int i = 0;

        for (Contact c : contacts) {
            if (c.getType().equals("Phone")) {
                i++;
            }
        }

        array = new Contact[i];
        i = 0;

        for (Contact c : contacts) {
            if (c.getType().equals("Phone")) {
                array[i] = c;
                i++;
            }
        }

        return array;
    }

    public Contact getLatestContact() {
        Contact contact = contacts[0];

        for (Contact c : contacts) {
            if (c.isNewerThan(contact)) {
                contact = c;
            }
        }

        return contact;
    }

    public int getContacts() {
        return contacts.length;
    }

    public String getCity() {
        return city;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public long getIndex() {
        return index;
    }
}

class PhoneContact extends Contact {
    private final String phone;
    private final Operator operator;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;

        char op = phone.charAt(2);

        if (op == '0' || op == '1' || op == '2') {
            operator = Operator.TMOBILE;
        } else if (op == '5' || op == '6') {
            operator = Operator.ONE;
        } else {
            operator = Operator.VIP;
        }
    }

    @Override
    public String toString() {
        return "\"" + phone + "\"";
    }

    @Override
    public String getType() {
        return "Phone";
    }

    public String getPhone() {
        return phone;
    }

    public Operator getOperator() {
        return operator;
    }
}

class Faculty {

    private final String name;
    private final Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = Arrays.copyOf(students, students.length);
    }

    @Override
    public String toString() {
        return "{\"fakultet\":\"" +
                name +
                "\", \"studenti\":" +
                Arrays.toString(students) +
                "}";
    }

    public int countStudentsFromCity(String city) {
        int s = 0;

        for (Student student : students) {
            if (student.getCity().equals(city)) {
                s++;
            }
        }

        return s;
    }

    Student getStudent(long index) {
        for (Student student : students) {
            if (student.getIndex() == index) {
                return student;
            }
        }

        return null;
    }

    public double getAverageNumberOfContacts() {
        double avg = 0;

        for (Student student : students) {
            avg += student.getContacts();
        }

        return avg / students.length;
    }

    public Student getStudentWithMostContacts() {
        int max = students[0].getContacts();
        Student s = students[0];

        for (Student student : students) {
            if (student.getContacts() > max || (student.getContacts() == max && s.getIndex() <= student.getIndex())) {
                max = student.getContacts();
                s = student;
            }
        }

        return s;
    }
}

class EmailContact extends Contact {

    private final String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    @Override
    public String toString() {
        return "\"" + email + "\"";
    }

    @Override
    public String getType() {
        return "Email";
    }

    public String getEmail() {
        return email;
    }
}

abstract class Contact {

    private final int year;
    private final int month;
    private final int day;

    public Contact(String date) {
        this.year = Integer.parseInt(date.split("-")[0]);
        this.month = Integer.parseInt(date.split("-")[1]);
        this.day = Integer.parseInt(date.split("-")[2]);
    }

    public abstract String getType();

    private long getDays() {
        return year * 365L + month * 30L + day;
    }

    public boolean isNewerThan(Contact c) {
        return getDays() > c.getDays();
    }
}

public class StudentContactsTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }
        }

        scanner.close();
    }
}