package SecondColloquium.ColloquiumAssignments.Airline;

import java.util.*;
import java.util.stream.Collectors;

class AirportNotFoundException extends Exception {

    public AirportNotFoundException(String airportCode) {
        // Airport CFRT2UI is not part of the company.
        super(String.format("Airport %s is not part of the company.", airportCode));
    }
}

class Airport implements Comparable<Airport> {

    private final String airportName;
    private final String airportCode;
    private final Map<String, Long> flights;

    public Airport(String airportName, String airportCode) {
        this.airportName = airportName;
        this.airportCode = airportCode;
        this.flights = new TreeMap<>();
    }

    public String getAirportName() {
        return airportName;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void addFlight(String flightCode, long duration) {
        flights.put(flightCode, duration);
    }

    public long getTotalFlightsDuration() {
        return flights.values().stream()
                .mapToLong(i -> i)
                .sum();
    }

    private int getCountFlights() {
        return flights.values().size();
    }

    @Override
    public int compareTo(Airport other) {
        return Comparator.comparing(Airport::getCountFlights)
                .reversed()
                .thenComparing(Airport::getAirportCode)
                .thenComparing(Airport::getAirportName)
                .compare(this, other);
    }

    @Override
    public String toString() {
        // BELGRADE: HJUUIPP
        // AWERTYU : 109 min
        // TJUHYTG : 222 min
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("%s: %s",
                        getAirportName(),
                        getAirportCode()))
                .append("\n");
        flights.entrySet().stream()
                .forEach(i ->
                        builder.append(String.format("%s : %d min",
                                        i.getKey(),
                                        i.getValue()))
                                .append("\n"));

        return builder.toString();
    }
}

class Airline {

    private final String airlineName;
    private final Map<String, Airport> airports;

    public Airline(String airlineName) {
        this.airlineName = airlineName;
        this.airports = new HashMap<>();
    }

    public void addAirport(String name, String code) {
        Airport newAirport = new Airport(name, code);
        airports.put(code, newAirport);
    }

    public void addFlight(String flightCodeFrom, String flightCodeTo, long duration) {
        airports.get(flightCodeFrom).addFlight(flightCodeTo, duration);
    }

    public void search(String code) throws AirportNotFoundException {
        if (!airports.containsKey(code)) {
            throw new AirportNotFoundException(code);
        }

        System.out.println(airports.get(code));
    }

    public long allFlights() {
        return airports.values().stream()
                .mapToLong(Airport::getTotalFlightsDuration)
                .sum();
    }

    public void printAirports() {
        airports.values().stream()
                .sorted()
                .forEach(System.out::println);
    }
}

public class AirlineTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in, "UTF-8");
        String name = scanner.nextLine();

        Airline airline = new Airline(name);

        int airportsNum = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < airportsNum; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            String airportName = parts[0];
            String code = parts[1];
            airline.addAirport(airportName, code);
        }

        int flightsNum = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < flightsNum; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            String code1 = parts[0];
            String code2 = parts[1];
            long duration = Long.parseLong(parts[2]);
            airline.addFlight(code1, code2, duration);
        }

        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 1) {
            String codeSearch = scanner.nextLine();
            System.out.println("SEARCH");

            try {
                airline.search(codeSearch);
            } catch (AirportNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else if (choice == 2) {
            System.out.println("ALL FLIGHTS");
            System.out.printf("%d\n", airline.allFlights());
        } else {
            System.out.println("ALL AIRPORTS");
            airline.printAirports();
        }

        scanner.close();
    }
}
