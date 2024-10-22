package SecondColloquium.ColloquiumAssignments._1Audition;

import java.util.*;

class Participant {

    private String city;
    private String code;
    private String name;
    private int age;

    public Participant(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Participant that = (Participant) object;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        // 003 Katarina 17
        return String.format("%s %s %d",
                getCode(),
                getName(),
                getAge());
    }
}

class Audition {

    private Map<String, TreeSet<Participant>> participantsByCity;
    private Map<String, HashSet<String>> codesByCity;

    public Audition() {
        this.participantsByCity = new HashMap<>();
        this.codesByCity = new HashMap<>();
    }

    public void addParticpant(String city, String code, String name, int age) {
        participantsByCity.putIfAbsent(city, new TreeSet<>(Comparator.comparing(Participant::getName)
                .thenComparing(Participant::getAge)
                .thenComparing(Participant::getCode)));
        codesByCity.putIfAbsent(city, new HashSet<>());

        Participant newParticipant = new Participant(city, code, name, age);

        if (!codesByCity.get(city).contains(newParticipant.getCode())) {
            participantsByCity.get(city).add(newParticipant);
        }

        codesByCity.get(city).add(newParticipant.getCode());
    }

    public void listByCity(String city) {
        participantsByCity.get(city)
                .forEach(participant -> System.out.println(participant));
    }
}

public class AuditionTest {

    public static void main(String[] args) {

        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}