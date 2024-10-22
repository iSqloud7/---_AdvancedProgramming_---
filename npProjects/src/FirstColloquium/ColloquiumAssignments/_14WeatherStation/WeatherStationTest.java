package FirstColloquium.ColloquiumAssignments._14WeatherStation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Measurement implements Comparable<Measurement> { // WeatherConditions

    private float temperature;
    private float humidity;
    private float wind;
    private float visibility;
    private Date date;

    public Measurement(float temperature, float humidity, float wind, float visibility, Date date) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.wind = wind;
        this.visibility = visibility;
        this.date = date;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getWind() {
        return wind;
    }

    public float getVisibility() {
        return visibility;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int compareTo(Measurement otherMeasurement) {
        return getDate().compareTo(otherMeasurement.getDate());
    }
}

class WeatherStation {

    private int days;
    private List<Measurement> measurements;

    // 2.5 mins in secs = ((2mins*60secs) + 30secs) = (120secs+30secs) = 150secs
    // 150secs in msecs [1sec -> 1 000msec] = 150secs*1000msecs = 150 000msecs
    private static final long TWO_AND_HALF_MINUTES_IN_MS = (2 * 60 + 30) * 1000; // 2.5mins is 150 000msecs

    /* WeatherConditionsFormat
           24.6     80.2 km/h   28.7%      51.7 km    Tue Dec 17 23:40:15 CET 2013
       temperature     wind    humidity  visibility          date:format ->  (dd.MM.yyyy HH:mm:ss)
    */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT' yyyy");

    public WeatherStation(int days) {
        this.days = days;
        this.measurements = new ArrayList<>();
    }

    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date) {
        // days * 24hours * 60minutes * 60seconds * 1 000milliseconds
        long daysInMS = (long) this.days * 24 * 60 * 60 * 1000;
        Date thresholdDate = new Date(date.getTime() - daysInMS);

        measurements.removeIf(measurement -> measurement.getDate().before(thresholdDate));

        for (Measurement measurement : measurements) {
            if (Math.abs(date.getTime() - measurement.getDate().getTime()) < TWO_AND_HALF_MINUTES_IN_MS) {
                return;
            }
        }

        measurements.add(new Measurement(temperature, wind, humidity, visibility, date));

        measurements = measurements.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public int total() {
        return measurements.size();
    }

    public void status(Date fromDate, Date toDate) {
        List<Measurement> rangeMeasurementsFromToPeriod = new ArrayList<>();

        for (Measurement measurement : measurements) {
            if (measurement.getDate().getTime() >= fromDate.getTime() &&
                    measurement.getDate().getTime() <= toDate.getTime()) {

                rangeMeasurementsFromToPeriod.add(measurement);
            }
        }

        if (rangeMeasurementsFromToPeriod.isEmpty()) {
            throw new RuntimeException();
        }

        /*
           // Format
           // 24.6 80.2 km/h 28.7% 51.7 km Tue Dec 17 23:40:15 CET 2013
           // 22.1 18.9 km/h  1.3% 24.6 km Tue Dec 17 23:30:15 GMT 2013
        */
        rangeMeasurementsFromToPeriod.stream()
                .forEach(measurement -> {
                    System.out.println(String.format("%.1f %.1f km/h %.1f%% %.1f km %s",
                            measurement.getTemperature(),
                            measurement.getHumidity(),
                            measurement.getWind(),
                            measurement.getVisibility(),
                            DATE_FORMAT.format(measurement.getDate())));
                });

        double averageTemperature = rangeMeasurementsFromToPeriod.stream()
                .mapToDouble(Measurement::getTemperature)
                .average()
                .orElse(0.0);

        // Average temperature: 20.43
        System.out.printf("Average temperature: %.2f", averageTemperature);
    }
}

public class WeatherStationTest {

    public static void main(String[] args) throws ParseException {

        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}