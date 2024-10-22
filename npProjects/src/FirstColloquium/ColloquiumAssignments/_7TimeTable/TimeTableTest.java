package FirstColloquium.ColloquiumAssignments._7TimeTable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

enum TimeFormat {

    FORMAT_24,
    FORMAT_AMPM
}

class UnsupportedFormatException extends Exception {

    public UnsupportedFormatException(String message) {
        super("UnsupportedFormatException: " + message);
    }
}

class InvalidTimeException extends Exception {

    public InvalidTimeException(String message) {
        super(message);
    }
}

class Time implements Comparable<Time> {

    private int hours;
    private int minutes;

    public Time(String line) throws UnsupportedFormatException, InvalidTimeException {
        // line = line.replace(".", ":");
        String[] parts;

        if (line.contains(":")) {
            parts = line.split(":");
        } else if (line.contains(".")) {
            parts = line.split("\\.");
        } else {
            throw new UnsupportedFormatException(line);
        }

        if (!isValidTie()) {
            throw new InvalidTimeException(line);
        }

        this.hours = Integer.parseInt(parts[0]);
        this.minutes = Integer.parseInt(parts[1]);
    }

    private boolean isValidTie() { // опсег (0-23, 0-59)
        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
            return false;
        }

        return true;
    }

    @Override
    public int compareTo(Time other) {
        if (this.hours != other.hours) {
            return this.hours - other.hours;
        }

        return this.minutes - other.minutes;
    }

    /*
       Правила за конверзија од 24-часовен формат во AM/PM:
       -за првиот час од денот (0:00 - 0:59), додадете 12 и направете го "AM"
       -од 1:00 до 11:59, само направето го "AM"
       -од 12:00 до 12:59, само направето го "PM"
       -од 13:00 до 23:59 одземете 12 и направете го "PM"
    */
    public void changeToAM_PM(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);

        if (hours == 0) {
            hours += 12;
            writer.println(this + " AM");
        } else if (hours > 0 && hours < 12) {
            writer.println(this + " AM");
        } else if (hours == 12) {
            writer.println(this + " PM");
        } else {
            hours -= 12;
            writer.println(this + " PM");
        }

        writer.flush();
    }

    @Override
    public String toString() {
        if (hours > 9) {
            return String.format("%d:%02d",
                    hours, minutes);
        } else {
            return String.format(" %d:%02d",
                    hours, minutes);
        }
    }
}

class TimeTable {

    private List<Time> times;

    public TimeTable() {
        this.times = new ArrayList<>();
    }

    // 11:15 0.45 23:12 15:29 18.46
    public void readTimes(InputStream inputStream) throws IOException, UnsupportedFormatException, InvalidTimeException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\s+");
            for (String part : parts) {
                try {
                    Time time = new Time(part);
                    times.add(time);
                } catch (UnsupportedFormatException | InvalidTimeException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        reader.close();
    }

    public void writeTimes(OutputStream outputStream, TimeFormat format) {
        PrintWriter writer = new PrintWriter(outputStream);

        if (format == TimeFormat.FORMAT_24) {
            times.stream()
                    .sorted()
                    .forEach(writer::println);
        } else {
            times.stream()
                    .sorted()
                    .forEach(time -> time.changeToAM_PM(outputStream));
        }

        writer.flush();
    }
}

public class TimeTableTest {

    public static void main(String[] args) throws IOException {

        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }
}