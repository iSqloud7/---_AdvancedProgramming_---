package SecondColloquium.ColloquiumAssignments._22EventCalendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

class WrongDateException extends Exception {

    public WrongDateException(String message) {
        super(message);
    }
}

class Event implements Comparable<Event> {

    private String name;
    private String location;
    private LocalDateTime date;
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMM, yyy HH:mm");

    public Event(String name, String location, LocalDateTime date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public int compareTo(Event other) {
        return Comparator.comparing(Event::getDate)
                .thenComparing(Event::getName)
                .compare(this, other);
    }

    @Override
    public String toString() {
        // dd MMM, YYY HH:mm at [location], [name]
        // 19 Apr, 2012 15:30 at FINKI, Brucoshka Zabava
        return String.format("%s at %s, %s",
                getDate().format(FORMATTER),
                getLocation(),
                getName());
    }
}

class EventCalendar {

    private int year;
    private Map<LocalDate, Set<Event>> eventsByDate;
    private Map<Integer, Integer> eventsByMonth;

    public EventCalendar(int year) {
        this.year = year;
        this.eventsByDate = new HashMap<>();
        this.eventsByMonth = new TreeMap<>();
        for (int i = 1; i <= 12; i++) {
            eventsByMonth.put(i, 0);
        }
    }

    public int getYear() {
        return year;
    }

    private LocalDate getDateFromLDT(LocalDateTime dateTime) {
        return dateTime.toLocalDate();
    }

    public void addEvent(String name, String location, LocalDateTime date) throws WrongDateException {
        if (date.getYear() != getYear()) {
            // Wrong date: [date]
            throw new WrongDateException(String.format("Wrong date: %s", date.format(DateTimeFormatter.ofPattern("eee MMM dd HH:mm:ss 'UTC' yyyy"))));
        }

        Event event = new Event(name, location, date);
        LocalDate keyDate = date.toLocalDate();

        eventsByDate.putIfAbsent(getDateFromLDT(date), new TreeSet<>());
        eventsByDate.get(getDateFromLDT(date)).add(event);

        eventsByMonth.computeIfPresent(date.getMonth().getValue(), (k, v) -> ++v);
    }

    // dd MMM, YYY HH:mm at [location], [name]
    // 19 Apr, 2012 15:30 at FINKI, Brucoshka Zabava
    public void listEvents(LocalDateTime date) {
        if (!eventsByDate.containsKey(getDateFromLDT(date))) {
            System.out.println("No events on this day!");
            return;
        }

        eventsByDate.get(getDateFromLDT(date))
                .forEach(System.out::println);
    }

    // 1 : 1
    // 2 : 2
    // 3 : 0
    // ...
    public void listByMonth() {
        eventsByMonth.entrySet()
                .forEach(IntIntEntry -> {
                    System.out.printf("%d : %d\n",
                            IntIntEntry.getKey(),
                            IntIntEntry.getValue());
                });
    }
}

public class EventCalendarTest {

    public static void main(String[] args) throws ParseException {

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        // DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            // Date date = df.parse(parts[2]);
            LocalDateTime date = LocalDateTime.parse(parts[2], formatter);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        // Date date = df.parse(scanner.nextLine());
        String scan = scanner.nextLine();
        LocalDateTime date = LocalDateTime.parse(scan, formatter);
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}