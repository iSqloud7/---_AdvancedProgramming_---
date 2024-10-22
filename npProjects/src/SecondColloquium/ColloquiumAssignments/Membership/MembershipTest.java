package SecondColloquium.ColloquiumAssignments.Membership;

import java.util.*;
import java.util.stream.Collectors;

class PersonalInformation {

    private final String country;
    private final Integer age;

    public PersonalInformation(String country, Integer age) {
        this.country = country;
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public Integer getAge() {
        return age;
    }
}

class User implements Comparable<User> {

    private static long userID = 1L;
    private final long ID;
    private final String username;
    private final String password;
    private final String email;
    private final PersonalInformation personalInformation;

    public User(String username, String password, String email, String country, Integer age) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.personalInformation = new PersonalInformation(country, age);
        this.ID = userID++;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public PersonalInformation getPersonalInformation() {
        return personalInformation;
    }

    public long getID() {
        return ID;
    }

    @Override
    public String toString() {
        // {id: userName, email}
        return String.format("{%d: %s, %s}",
                ID,
                getUsername(),
                getEmail());
    }

    @Override
    public int compareTo(User other) {
        return Comparator.comparing(User::getEmail)
                .compare(this, other);
    }
}

class Membership {

    private static final List<User> users = new ArrayList<>();


    public static void createUser(String username, String password, String email, String country, Integer age) {
        users.add(new User(username, password, email, country, age));
    }

    public static boolean deleteUser(Integer ID) {
        return users.removeIf(u -> u.getID() == ID);
    }

    public static List<User> getUsersOrderedByEmail() {
        return users.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public static Map<String, Integer> getNDifferentUsersByEmail() {
        /* TreeMap<>()
        Map<String, Integer> usersByEmail = new TreeMap<>();
        ListIterator<User> userListIterator = users.listIterator();
        while (userListIterator.hasNext()) {
            User user = userListIterator.next();
            if (!usersByEmail.containsKey(user.getEmail())) {
                usersByEmail.put(user.getEmail(), 1);
            } else {
                usersByEmail.put(user.getEmail(), usersByEmail.get(user.getEmail()) + 1);
            }
        }

        return usersByEmail;
        */

        /* instead ListIterator<>
        for (User user : users) {
            if (!usersByEmail.containsKey(user.getEmail())) {
                usersByEmail.put(user.getEmail(), 1);
            } else {
                usersByEmail.put(user.getEmail(), usersByEmail.get(user.getEmail()) + 1);
            }
        }
        */

        // groupingBy()
        return users.stream()
                .collect(Collectors.groupingBy(
                        user -> user.getEmail(),
                        TreeMap::new,
                        Collectors.summingInt(user -> 1)
                ));
    }
}

public class MembershipTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int length = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < length; i++) {
            String userName = scanner.nextLine();
            String password = scanner.nextLine();
            String email = scanner.nextLine();
            String country = scanner.nextLine();
            Integer age = scanner.nextInt();
            scanner.nextLine();

            Membership.createUser(userName, password, email, country, age);
        }

        for (int i = 0; i < length; i++) {
            System.out.println(Membership.getNDifferentUsersByEmail());
            System.out.println(Membership.getUsersOrderedByEmail());
            System.out.println(Membership.deleteUser(i + 1));
        }

        System.out.println(Membership.deleteUser(1));
    }
}