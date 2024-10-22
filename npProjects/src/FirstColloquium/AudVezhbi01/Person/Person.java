package FirstColloquium.AudVezhbi01.Person;

import java.util.Objects;

public class Person {

    private String name;
    private String surname;
    private int age;

    public Person() {
    }

    public Person(String name, String surname, int age) {
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Person person = (Person) object;
        return age == person.age && Objects.equals(name, person.name) && Objects.equals(surname, person.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, age);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                '}';
    }

    public static void main(String[] args) {

        Person person1 = new Person(); // constructor with no arguments -> default constructor
        Person person2 = new Person("Ivan", "Pupinoski", 21); // constructor with arguments
        Person person3 = new Person("Ivan", "Pupinoski", 20);

        // strings and all class objects are compared with equals
        if (person2.equals(person3)) { // person2 == person3 -> comparing references
            System.out.println("true");
        } else {
            System.out.println("false");
        }

        System.out.println(person2);
    }
}
