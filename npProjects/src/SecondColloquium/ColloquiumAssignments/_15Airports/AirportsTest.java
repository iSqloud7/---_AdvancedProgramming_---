package SecondColloquium.ColloquiumAssignments._15Airports;

import java.util.*;
import java.util.stream.Collectors;

class Airport {

    private String airportName;
    private String airportCountry;
    private String airportCode;
    private int airportPassengers;

    public Airport(String airportName, String airportCountry, String airportCode, int airportPassengers) {
        this.airportName = airportName;
        this.airportCountry = airportCountry;
        this.airportCode = airportCode;
        this.airportPassengers = airportPassengers;
    }

    public String getAirportName() {
        return airportName;
    }

    public String getAirportCountry() {
        return airportCountry;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public int getAirportPassengers() {
        return airportPassengers;
    }
}

class Flight {

    private String departureCode;
    private String landingCode;
    private int departureTime;
    private int flightDuration;

    public Flight(String departureCode, String landingCode, int departureTime, int flightDuration) {
        this.departureCode = departureCode;
        this.landingCode = landingCode;
        this.departureTime = departureTime;
        this.flightDuration = flightDuration;
    }

    public String getDepartureCode() {
        return departureCode;
    }

    public String getLandingCode() {
        return landingCode;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public int getFlightDuration() {
        return flightDuration;
    }

    public String getTimeOfDeparture(int time) {
        int hours = time / 60;
        int minutes = time % 60;

        return String.format("%02d:%02d", hours, minutes);
    }

    public String getTimeOfLanding() {
        int timeOfLanding = getDepartureTime() + flightDuration;
        int minutesInOneDay = timeOfLanding % 1440; // 24hours * 60minutes = 1440minutes in one day
        boolean isNextDay = timeOfLanding >= 1440;

        int hours = minutesInOneDay / 60;
        int minutes = minutesInOneDay % 60;

        return String.format("%02d:%02d", hours, minutes) + (isNextDay ? " +1d" : "");
    }

    public String getFlightDurationFormatted() {
        int hours = getFlightDuration() / 60;
        int minutes = getFlightDuration() % 60;

        return String.format("%dh%02dm", hours, minutes);
    }
}

class Airports {

    private Map<String, Airport> airportsMap;
    private Map<String, List<Flight>> flightsFrom;
    private Map<String, List<Flight>> flightsTo;

    public Airports() {
        this.airportsMap = new HashMap<>();
        this.flightsFrom = new HashMap<>();
        this.flightsTo = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        Airport airport = new Airport(name, country, code, passengers);
        airportsMap.put(code, airport);

        flightsFrom.putIfAbsent(code, new ArrayList<>());
        /*
        if (!flightsFrom.containsKey(code)) {
            flightsFrom.put(code, new ArrayList<>());
        }
        */

        flightsTo.putIfAbsent(code, new ArrayList<>());
        /*
        if (!flightsTo.containsKey(code)) {
            flightsTo.put(code, new ArrayList<>());
        }
        */
    }

    public void addFlights(String from, String to, int time, int duration) {
        Flight flight = new Flight(from, to, time, duration);

        flightsFrom.computeIfAbsent(from, k -> new ArrayList<>()).add(flight);
        /*
        List<Flight> departureList = flightsFrom.get(from);
        if (departureList == null) {
            departureList = new ArrayList<>();
            flightsFrom.put(from, departureList);
        }

        departureList.add(flight);
        */

        flightsTo.computeIfAbsent(to, k -> new ArrayList<>()).add(flight);
        /*
        List<Flight> landingList = flightsTo.get(to);
        if (landingList == null) {
            landingList = new ArrayList<>();
            flightsTo.put(to, landingList);
        }

        landingList.add(flight);
        */
    }

    /* Format:
       ===== FLIGHTS FROM HND =====
       Tokyo International (HND)
       Japan
       66795178
       1. HND-AMS 14:44-19:16 4h32m
       2. HND-DFW 17:28-22:20 4h52m
       3. HND-PEK 04:59-06:49 1h50m
       4. HND-PVG 21:13-01:29 +1d 4h16m
    */
    public void showFlightsFromAirport(String code) {
        Airport airport = airportsMap.get(code);
        if (airport == null)
            return;

        System.out.printf("%s (%s)\n%s\n%d\n",
                airport.getAirportName(), airport.getAirportCode(), airport.getAirportCountry(), airport.getAirportPassengers());

        List<Flight> flights = flightsFrom.get(code);
        if (flights == null || flights.isEmpty())
            return;

        int[] ordNumber = {1};
        flights.stream() // според кодот на аеродромот дестинација, па според времето на полетување
                .sorted((flight1, flight2) -> {
                    int result = flight1.getLandingCode().compareTo(flight2.getLandingCode());
                    if (result == 0) {
                        return Integer.compare(flight1.getDepartureTime(), flight2.getDepartureTime());
                    }

                    return result;
                })
                .forEach(flight -> {
                    System.out.printf("%d. %s-%s %s-%s %s\n",
                            ordNumber[0]++,
                            flight.getDepartureCode(), // from
                            flight.getLandingCode(), // to
                            flight.getTimeOfDeparture(flight.getDepartureTime()),
                            flight.getTimeOfLanding(),
                            flight.getFlightDurationFormatted());
                });

    }

    public void showDirectFlightsFromTo(String from, String to) {
        List<Flight> flights = flightsFrom.get(from);
        if (flights == null || flights.isEmpty())
            return;

        List<Flight> directFlights = flights.stream()
                .filter(flight -> flight.getLandingCode().equals(to))
                .collect(Collectors.toList());

        if (directFlights == null || directFlights.isEmpty()) {
            // ===== DIRECT FLIGHTS FROM LHR TO DFW =====
            // No flights from HND to IAH
            System.out.printf("No flights from %s to %s\n",
                    from, to);
        } else {
            // ===== DIRECT FLIGHTS FROM LHR TO DFW =====
            // LHR-DFW 21:20-23:45 2h25m
            System.out.printf("%s-%s ", from, to);

            directFlights.stream()
                    .forEach(flight -> {
                        System.out.printf("%s-%s %s\n",
                                flight.getTimeOfDeparture(flight.getDepartureTime()),
                                flight.getTimeOfLanding(),
                                flight.getFlightDurationFormatted());
                    });
        }
    }

    public void showDirectFlightsTo(String to) {
        List<Flight> flights = flightsTo.get(to);
        if (flights == null || flights.isEmpty())
            return;

        // ===== DIRECT FLIGHTS TO ATL =====
        // PHX-ATL 00:22-02:55 2h33m
        // DEN-ATL 03:32-05:06 1h34m
        flights.stream()
                .sorted((flight1, flight2) -> {
                    int result = Double.compare(flight1.getDepartureTime(), flight2.getDepartureTime());
                    if (result == 0) {
                        return flight1.getDepartureCode().compareTo(flight2.getDepartureCode());
                    }

                    return result;
                })
                .forEach(flight -> {
                    System.out.printf("%s-%s %s-%s %s\n",
                            flight.getDepartureCode(),
                            flight.getLandingCode(),
                            flight.getTimeOfDeparture(flight.getDepartureTime()),
                            flight.getTimeOfLanding(),
                            flight.getFlightDurationFormatted());
                });
    }
}

public class AirportsTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}