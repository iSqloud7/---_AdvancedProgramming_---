package SecondColloquium.ColloquiumAssignments._10StopCoronaApp;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

interface ILocation {
    double getLongitude();

    double getLatitude();

    LocalDateTime getTimestamp();
}

class UserIdAlreadyExistsException extends Exception {

    public UserIdAlreadyExistsException(String message) {
        super(message);
    }
}

class UserContact {

    private final User user;
    private final int contactCount;

    public UserContact(User user, int contactCount) {
        this.user = user;
        this.contactCount = contactCount;
    }

    public User getUser() {
        return user;
    }

    public int getContactCount() {
        return contactCount;
    }
}

class User {

    private String userName;
    private String userID;
    private List<ILocation> iLocations;
    private LocalDateTime timestamp;
    private boolean isSick;
    static Comparator<User> comparator = Comparator.comparing(user -> user.userName);

    public User(String userName, String userID) {
        this.userName = userName;
        this.userID = userID;
        this.iLocations = new ArrayList<>();
        this.timestamp = LocalDateTime.MAX;
        this.isSick = false;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserID() {
        return userID;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void addLocations(List<ILocation> iLocations) {
        this.iLocations.addAll(iLocations);
    }

    public void registerSick(LocalDateTime localDateTime) {
        this.timestamp = localDateTime;
        isSick = true;
    }

    public int closeContacts(User other) {
        int counter = 0;
        for (ILocation thisiLocation : this.iLocations) {
            for (ILocation otheriLocation : other.iLocations) {
                double distance = Math.sqrt(Math.pow(thisiLocation.getLatitude() - otheriLocation.getLatitude(), 2) +
                        Math.pow(thisiLocation.getLongitude() - otheriLocation.getLongitude(), 2));
                Duration duration = Duration.between(thisiLocation.getTimestamp(), otheriLocation.getTimestamp());

                if (distance <= 2 && duration.getSeconds() < 300) {
                    counter++;
                }
            }
        }

        return counter;
    }
}

class StopCoronaApp {

    private Map<String, User> users;

    public StopCoronaApp() {
        this.users = new HashMap<>();
    }

    public void addUser(String name, String id) throws UserIdAlreadyExistsException {
        if (users.containsKey(id)) {
            throw new UserIdAlreadyExistsException("USER EXIST");
        }

        users.put(id, new User(name, id));
    }

    public void addLocations(String id, List<ILocation> iLocations) {
        users.get(id).addLocations(iLocations);
    }

    public void detectNewCase(String id, LocalDateTime timestamp) {
        users.get(id).registerSick(timestamp);
    }

    public Map<User, Integer> getDirectContacts(User u) {
        List<UserContact> contactList = users.values().stream()
                .filter(user -> !user.equals(u))
                .map(user -> new UserContact(user, u.closeContacts(user)))
                .filter(userContact -> userContact.getContactCount() > 0)
                .sorted((uc1, uc2) -> Integer.compare(uc2.getContactCount(), uc1.getContactCount()))
                .collect(Collectors.toList());

        Map<User, Integer> resultMap = new LinkedHashMap<>();
        for (UserContact userContact : contactList) {
            resultMap.put(userContact.getUser(), userContact.getContactCount());
        }

        return resultMap;
    }


    public Collection<User> getIndirectContacts(User u) {
        Set<User> directContacts = getDirectContacts(u).keySet();

        return users.values().stream()
                .filter(user -> !user.equals(u) && !directContacts.contains(user))
                .filter(user -> !getDirectContacts(user).keySet().containsAll(directContacts))
                .sorted(User.comparator.thenComparing(User::getUserID))
                .collect(Collectors.toList());
    }

    /*
       [user_name] [user_id] [timestamp_detected]
       Direct contacts:
       [contact1_name] [contact1_first_five_letters_of_id] [number_of_detected_contacts1]
       [contact2_name] [contact2_first_five_letters_of_id] [number_of_detected_contacts1]
       ...
       [contactN_name] [contactN_first_five_letters_of_id] [number_of_detected_contactsN]
       Count of direct contacts: [sum]
       Indirect contacts:
       [contact1_name] [contact1_first_five_letters_of_id]
       [contact2_name] [contact2_first_five_letters_of_id]
       ...
       [contactN_name] [contactN_first_five_letters_of_id]
       Count of indirect contacts: [count]
       Дополнително на крајот на извештајот да се испечати:
       просечниот број на директни и индиректни контакти на корисниците што се носители на Корона вирусот.

       Format:
       Anastasija 044290ed-8ae6-4cc1-99dc-232e20fa905b 2020-06-08T22:58:11.953
       Direct contacts:
       Anastasija c717*** 2
       Trajce 9be0*** 1
       ...
       Count of direct contacts: 3
       Indirect contacts:
       Sofija 5964***
       Count of indirect contacts: 1
       Sofija 596462ec-43b2-4015-a9a0-aad1a9b7cd93 2020-06-09T22:40:11.953
       Direct contacts:
       Trajce 9be0*** 2
       Anastasija c717*** 1
       Count of direct contacts: 3
       Indirect contacts:
       Anastasija 0442***
       Count of indirect contacts: 1
       Average direct contacts: 3.0000
       Average indirect contacts: 1.0000
    */
    public void createReport() {
        users.values().stream()
                .sorted(Comparator.comparing(u -> u.getTimestamp()))
                .filter(user -> user.getTimestamp() != LocalDateTime.MAX)
                .forEach(user -> {
                    System.out.println(String.format("%s %s %s",
                            user.getUserName(), user.getUserID(), user.getTimestamp()));
                    System.out.println("Direct contacts:");
                    getDirectContacts(user).keySet().stream()
                            .forEach(u2 -> System.out.println(String.format("%s %s %d",
                                    u2.getUserName(), u2.getUserID().substring(0, 6), u2.closeContacts(user))));
                    getIndirectContacts(user).stream()
                            .forEach(u3 -> System.out.println(String.format("%s %s",
                                    u3.getUserName(), u3.getUserID().substring(0, 6))));

                });
    }
}

public class StopCoronaAppTest {

    public static double timeBetweenInSeconds(ILocation location1, ILocation location2) {
        return Math.abs(Duration.between(location1.getTimestamp(), location2.getTimestamp()).getSeconds());
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        StopCoronaApp stopCoronaApp = new StopCoronaApp();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            switch (parts[0]) {
                case "REG": //register
                    String name = parts[1];
                    String id = parts[2];
                    try {
                        stopCoronaApp.addUser(name, id);
                    } catch (UserIdAlreadyExistsException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "LOC": //add locations
                    id = parts[1];
                    List<ILocation> locations = new ArrayList<>();
                    for (int i = 2; i < parts.length; i += 3) {
                        locations.add(createLocationObject(parts[i], parts[i + 1], parts[i + 2]));
                    }
                    stopCoronaApp.addLocations(id, locations);

                    break;
                case "DET": //detect new cases
                    id = parts[1];
                    LocalDateTime timestamp = LocalDateTime.parse(parts[2]);
                    stopCoronaApp.detectNewCase(id, timestamp);

                    break;
                case "REP": //print report
                    stopCoronaApp.createReport();
                    break;
                default:
                    break;
            }
        }
    }

    private static ILocation createLocationObject(String lon, String lat, String timestamp) {
        return new ILocation() {
            @Override
            public double getLongitude() {
                return Double.parseDouble(lon);
            }

            @Override
            public double getLatitude() {
                return Double.parseDouble(lat);
            }

            @Override
            public LocalDateTime getTimestamp() {
                return LocalDateTime.parse(timestamp);
            }
        };
    }
}