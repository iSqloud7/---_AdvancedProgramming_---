package SecondColloquium.ColloquiumAssignments.Parking;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class ParkingRecord {

    private String registration;
    private String spot;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public ParkingRecord(String registration, String spot, LocalDateTime entryTime) {
        this.registration = registration;
        this.spot = spot;
        this.entryTime = entryTime;
    }

    public String getRegistration() {
        return registration;
    }

    public String getSpot() {
        return spot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public long getDuration() {
        if (exitTime == null) {
            return 0;
        }
        return DateUtil.durationBetween(entryTime, exitTime);
    }
}

class DateUtil {

    public static long durationBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }
}

class Parking {

    private int capacity;
    private Map<String, ParkingRecord> currentParkedVehicles;
    private Map<String, LocalDateTime> spotAssignments;
    private Map<String, Integer> carStatistics;
    private Map<String, List<ParkingRecord>> vehicleHistory;

    public Parking(int capacity) {
        this.capacity = capacity;
        this.currentParkedVehicles = new HashMap<>();
        this.spotAssignments = new HashMap<>();
        this.carStatistics = new HashMap<>();
        this.vehicleHistory = new HashMap<>();
    }

    public void update(String registration, String spot, LocalDateTime timestamp, boolean entry) {
        if (entry) {
            if (currentParkedVehicles.size() < capacity) {
                ParkingRecord record = new ParkingRecord(registration, spot, timestamp);
                currentParkedVehicles.put(registration, record);
                spotAssignments.put(spot, timestamp);
                carStatistics.put(registration, carStatistics.getOrDefault(registration, 0) + 1);
            }
        } else {
            ParkingRecord record = currentParkedVehicles.remove(registration);
            if (record != null) {
                record.setExitTime(timestamp);
                vehicleHistory.computeIfAbsent(registration, k -> new ArrayList<>()).add(record);
                spotAssignments.remove(record.getSpot());
            }
        }
    }

    public void currentState() {
        double occupancyPercentage = (double) currentParkedVehicles.size() / capacity * 100;
        // Capacity filled: 50.00%
        System.out.printf("Capacity filled: %.2f%%\n", occupancyPercentage);

        // Registration number: NE0002AA Spot: A4 Start timestamp: 2024-01-15T21:11:32
        currentParkedVehicles.values().stream()
                .sorted(Comparator.comparing(ParkingRecord::getEntryTime).reversed())
                .forEach(record -> System.out.printf("Registration number: %s Spot: %s Start timestamp: %s\n",
                        record.getRegistration(),
                        record.getSpot(),
                        record.getEntryTime()));
    }

    public void history() {
        // Registration number: NE0003AA Spot: A7 Start timestamp: 2024-01-15T17:45:32 End timestamp: 2024-01-15T20:45:32 Duration in minutes: 180
        vehicleHistory.values().stream()
                .flatMap(List::stream)
                .sorted(Comparator.comparing(ParkingRecord::getDuration).reversed())
                .forEach(record -> System.out.printf("Registration number: %s Spot: %s Start timestamp: %s End timestamp: %s Duration in minutes: %d\n",
                        record.getRegistration(),
                        record.getSpot(),
                        record.getEntryTime(),
                        record.getExitTime(),
                        record.getDuration()));
    }

    public Map<String, Integer> carStatistics() {
        return new TreeMap<>(carStatistics);
    }

    public Map<String, Double> spotOccupancy(LocalDateTime start, LocalDateTime end) {
        Map<String, Double> spotOccupancy = new TreeMap<>();
        Map<String, Long> spotTotalTime = new HashMap<>();
        Map<String, Long> spotOccupiedTime = new HashMap<>();

        // Iterate over the current assignments to calculate occupied time
        for (Map.Entry<String, LocalDateTime> entry : spotAssignments.entrySet()) {
            String spot = entry.getKey();
            LocalDateTime entryTime = entry.getValue();

            // Calculate the actual time range for this spot
            LocalDateTime effectiveStart = entryTime.isBefore(start) ? start : entryTime;
            LocalDateTime effectiveEnd = end.isBefore(LocalDateTime.now()) ? end : LocalDateTime.now();

            if (effectiveStart.isBefore(effectiveEnd)) {
                long totalMinutes = Duration.between(entryTime, LocalDateTime.now()).toMinutes();
                long occupiedMinutes = Duration.between(effectiveStart, effectiveEnd).toMinutes();

                spotTotalTime.put(spot, spotTotalTime.getOrDefault(spot, totalMinutes));
                spotOccupiedTime.put(spot, spotOccupiedTime.getOrDefault(spot, 0L) + occupiedMinutes);
            }
        }

        // Calculate the occupancy percentage for each spot
        for (String spot : spotTotalTime.keySet()) {
            long total = spotTotalTime.get(spot);
            long occupied = spotOccupiedTime.getOrDefault(spot, 0L);
            double occupancy = (double) occupied / total * 100;
            spotOccupancy.put(spot, occupancy);
        }

        return spotOccupancy;
    }

}

public class ParkingTest {

    public static <K, V extends Comparable<V>> void printMapSortedByValue(Map<K, V> map) {
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(String.format("%s -> %s", entry.getKey().toString(), entry.getValue().toString())));

    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int capacity = Integer.parseInt(sc.nextLine());

        Parking parking = new Parking(capacity);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equals("update")) {
                String registration = parts[1];
                String spot = parts[2];
                LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
                boolean entrance = Boolean.parseBoolean(parts[4]);
                parking.update(registration, spot, timestamp, entrance);
            } else if (parts[0].equals("currentState")) {
                System.out.println("PARKING CURRENT STATE");
                parking.currentState();
            } else if (parts[0].equals("history")) {
                System.out.println("PARKING HISTORY");
                parking.history();
            } else if (parts[0].equals("carStatistics")) {
                System.out.println("CAR STATISTICS");
                printMapSortedByValue(parking.carStatistics());
            } else if (parts[0].equals("spotOccupancy")) {
                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);
                printMapSortedByValue(parking.spotOccupancy(start, end));
            }
        }
    }
}