package FirstColloquium.ColloquiumAssignments._21F1Race;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class Lap implements Comparable<Lap> {

    // lap е во формат mm:ss:nnn

    private int minutes;
    private int seconds;
    private int milliseconds;

    public Lap(String line) {
        String[] parts = line.split(":");

        this.minutes = Integer.parseInt(parts[0]);
        this.seconds = Integer.parseInt(parts[1]);
        this.milliseconds = Integer.parseInt(parts[2]);
    }

    @Override
    public int compareTo(Lap other) {
        if (this.minutes != other.minutes) {
            return this.minutes - other.minutes;
        }
        if (this.seconds != other.seconds) {
            return this.seconds - other.seconds;
        }
        if (this.milliseconds != other.milliseconds) {
            return this.milliseconds - other.milliseconds;
        }

        return 0;
    }

    @Override
    public String toString() {
        return String.format("%d:%02d:%03d",
                minutes, seconds, milliseconds);
    }
}

class F1Driver implements Comparable<F1Driver> {

    private String name;
    private List<Lap> laps;

    public F1Driver(String line) {
        // Driver_name lap1 lap2 lap3
        String[] parts = line.split("\\s+");

        this.name = parts[0];
        this.laps = new ArrayList<>(3);

        for (int i = 1; i <= 3; i++) {
            this.laps.add(new Lap(parts[i]));
        }
    }

    public Lap getFastestLap() {
        return Collections.min(laps);
    }

    @Override
    public int compareTo(F1Driver other) {
        return this.getFastestLap().compareTo(other.getFastestLap());
    }

    @Override
    public String toString() {
        // Driver_name best_lap со 10 места за името на возачот (порамнето од лево) и
        // 10 места за времето на најдобриот круг порамнето од десно

        return String.format("%-10s%10s",
                name, getFastestLap());
    }
}

class F1Race {

    private List<F1Driver> f1Drivers;

    public F1Race() {
        this.f1Drivers = new ArrayList<>();
    }

    public void readResults(InputStream inputStream) throws IOException {
        // Driver_name lap1 lap2 lap3, притоа lap е во формат mm:ss:nnn

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        f1Drivers = reader.lines()
                .map(F1Driver::new)
                .collect(Collectors.toList());
    }

    public void printSorted(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);

        Collections.sort(f1Drivers);

        for (int i = 0; i < f1Drivers.size(); i++) {
            writer.println(i + 1 + ". " + f1Drivers.get(i));
        }

        /*
        f1Drivers.stream()
                .sorted()
                .forEach(i -> writer.println(i.toString()));
        */

        writer.flush();
    }
}

public class F1RaceTest {

    public static void main(String[] args) {

        F1Race f1Race = new F1Race();
        try {
            f1Race.readResults(System.in);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        f1Race.printSorted(System.out);
    }
}