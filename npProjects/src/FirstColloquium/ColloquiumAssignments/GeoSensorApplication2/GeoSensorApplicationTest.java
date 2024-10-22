package FirstColloquium.ColloquiumAssignments.GeoSensorApplication2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

interface IGeo {

    double getLatitude();

    double getLongitude();

    default double distance(IGeo other) {

        return Math.sqrt(Math.pow(this.getLatitude() - other.getLatitude(), 2) +
                Math.pow(this.getLongitude() - other.getLongitude(), 2));
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

class BadSensorException extends Exception {

    private String sensorID;

    public BadSensorException(String sensorID) {
        super(String.format("No readings for sensor: %s",
                sensorID));
    }
}

class Measurement {

    protected int timestamp;
    private float value;

    public Measurement(int timestamp, float value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public float getValue() {
        return value;
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

        List<Measurement> measurements = new ArrayList<>();

        for (int i = 3; i < parts.length; i++) {
            measurements.add(Measurement.createMeasurement(parts[i], sensorID));
        }

        return new Sensor(sensorID, location, measurements);
    }

    public String getSensorID() {
        return sensorID;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public double averageValue() {
        return measurements.stream()
                .mapToDouble(Measurement::getValue)
                .average()
                .orElse(0.0);
    }

    public double averageValue(IGeo location, double distance, long from, long to) {
        if (this.location.distance(location) > distance) {
            return -1.0;
        }

        return measurements.stream()
                .filter(m -> m.timestamp >= from && m.timestamp < to)
                .mapToDouble(m -> m.getValue())
                .average()
                .orElse(-1.0);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "sensorID='" + sensorID + '\'' +
                '}';
    }
}

class ExtremeValue {

    private String sensorID;
    private Measurement measurement;
    private boolean isMin;

    public ExtremeValue(String sensorID, Measurement measurement, boolean isMin) {
        this.sensorID = sensorID;
        this.measurement = measurement;
        this.isMin = isMin;
    }

    public String getSensorID() {
        return sensorID;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public boolean isMin() {
        return isMin;
    }

    @Override
    public String toString() {
        String type = isMin ? "min" : "max";
        return String.format("ExtremeValue{sensorID='%s', %s=Measurement{timestamp=%d, value=%.1f}}",
                sensorID, type, measurement.timestamp, measurement.getValue());
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

    public double averageValue() {
        return sensors.stream()
                .mapToDouble(Sensor::averageValue)
                .average()
                .orElse(0);
    }

    public double averageDistanceValue(IGeo location, double distance, long t1, long t2) {
        return sensors.stream()
                .mapToDouble(sensor -> sensor.averageValue(location, distance, t1, t2))
                .filter(average -> average >= 0)
                .average()
                .orElse(0.0);
    }

    public List<ExtremeValue> extremeValues(long timeFrom, long timeTo) {
        List<ExtremeValue> extremeValues = new ArrayList<>();

        for (Sensor sensor : sensors) {
            List<Measurement> measurements = sensors.getFirst().getMeasurements()
                    .stream()
                    .filter(measurement -> measurement.timestamp >= timeFrom && measurement.timestamp <= timeFrom)
                    .collect(Collectors.toList());

            if (measurements.isEmpty()) {
                return Collections.emptyList();
            }

            double minVal = measurements.stream()
                    .mapToDouble(Measurement::getValue)
                    .min()
                    .orElse(Double.MAX_VALUE);
            double maxVal = measurements.stream()
                    .mapToDouble(Measurement::getValue)
                    .max()
                    .orElse(Double.MIN_VALUE);

            for (Measurement measurement : measurements) {
                if (measurement.getValue() == minVal) {
                    extremeValues.add(new ExtremeValue(sensor.getSensorID(), measurement, true));
                }
                if (measurement.getValue() == maxVal) {
                    extremeValues.add(new ExtremeValue(sensor.getSensorID(), measurement, false));
                }
            }
        }

        return extremeValues;
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

            System.out.println(app.averageValue());
            System.out.println(app.averageDistanceValue(new IGeo() {
                @Override
                public double getLatitude() {
                    return lat;
                }

                @Override
                public double getLongitude() {
                    return lon;
                }
            }, dis, t1, t2));

            System.out.println(app.extremeValues(t1, t2));
        } catch (BadSensorException e) {
            System.out.println(e.getMessage());
        } catch (BadMeasureException e) {
            System.out.println(e.getMessage());
        } finally {
            s.close();
        }
    }
}