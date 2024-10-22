package FirstColloquium.ColloquiumAssignments.GeoSensorApplication1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Measurement {

    private int timestamp;
    private float value;

    public Measurement(int timestamp, float value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public static Measurement createMeasurement(String data, String sensorID) throws BadMeasureException {
        String[] parts = data.split(":");
        // timestamp1:value1 -> [6:3] timestamp2:value2 -> [7:9] ...

        int timestamp = Integer.parseInt(parts[0]);
        float value = Float.parseFloat(parts[1]);

        if (value < 0) {
            throw new BadMeasureException(timestamp, sensorID);
        }

        return new Measurement(timestamp, value);
    }
}

interface IGeo {

    double getLatitude();

    double getLongitude();

    default double distance(IGeo other) {

        return Math.sqrt(Math.pow(this.getLatitude() - other.getLatitude(), 2) +
                Math.pow(this.getLongitude() - other.getLongitude(), 2));
    }
}

class Sensor {

    private String sensorID;
    protected IGeo location;
    private List<Measurement> measurements;

    public Sensor(String sensorID, IGeo location, List<Measurement> measurements) {
        this.sensorID = sensorID;
        this.location = location;
        this.measurements = measurements;
    }

    public static Sensor createSensor(String line) throws BadMeasureException, BadSensorException {
        String[] parts = line.split("\\s+");
        // sensorId -> [2] sensorLatitude -> [2] sensorLongitude -> [4] timestamp1:value1 -> [6:3] timestamp2:value2 -> [7:9] ...


        String sensorID = parts[0];

        if (parts.length == 3) {
            throw new BadSensorException(sensorID);
        }

        IGeo location = new IGeo() {
            @Override
            public double getLatitude() {
                return Double.parseDouble(parts[1]);
            }

            @Override
            public double getLongitude() {
                return Double.parseDouble(parts[2]);
            }
        };

        /* streams
        List<Measurement> measurements = new ArrayList<>();
        measurements = Arrays.stream(parts)
                .skip(3)
                .map(Measurement::createMeasurement)
                .collect(Collectors.toList());
         */

        // loop
        List<Measurement> measurements = new ArrayList<>();
        long toSkip = 3;

        for (String part : parts) {
            if (toSkip > 0) {
                toSkip--;
                continue;
            }

            Measurement measurement = Measurement.createMeasurement(part, sensorID);
            measurements.add(measurement);
        }

        return new Sensor(sensorID, location, measurements);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "sensorID='" + sensorID + '\'' +
                '}';
    }
}

class BadSensorException extends Exception {

    private String sensorID;

    public BadSensorException(String sensorID) {
        super(String.format("No readings for sensor: %s",
                sensorID));
    }
}

class BadMeasureException extends Exception {

    private int timestamp;
    private String sensorID;

    public BadMeasureException(int timestamp, String sensorID) {
        this.timestamp = timestamp;
        this.sensorID = sensorID;
    }

    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    @Override
    public String getMessage() {
        return String.format("Error in timestamp: %d from sensor: %s",
                timestamp, sensorID);
    }
}

class GeoSensorApplication {

    private List<Sensor> sensors;

    public GeoSensorApplication() {
        this.sensors = new ArrayList<>();
    }

    public void readGeoSensors(Scanner scanner) throws BadSensorException, BadMeasureException {

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            sensors.add(Sensor.createSensor(line));
        }
    }

    public List<Sensor> inRange(IGeo location, double distance) {
        return sensors.stream()
                .filter(sensor -> sensor.location.distance(location) < distance)
                .collect(Collectors.toList());
    }
}

public class GeoSensorApplicationTest {

    public static void main(String[] args) {

        GeoSensorApplication app = new GeoSensorApplication();

        Scanner s = new Scanner(System.in);
        double lat = s.nextDouble();
        double lon = s.nextDouble();
        double dis = s.nextDouble();
        long t1 = s.nextLong();
        long t2 = s.nextLong();

        s.nextLine();

        System.out.println("Access point on {" + lat + ", " + lon + "} distance:" + dis + " from:" + t1 + " - to:" + t2);

        try {
            app.readGeoSensors(s);

            System.out.println(app.inRange(new IGeo() {
                @Override
                public double getLatitude() {
                    return lat;
                }

                @Override
                public double getLongitude() {
                    return lon;
                }
            }, dis));

        } catch (BadSensorException e) {
            System.out.println(e.getMessage());
        } catch (BadMeasureException e) {
            System.out.println(e.getMessage());
        } finally {
            s.close();
        }
    }
}