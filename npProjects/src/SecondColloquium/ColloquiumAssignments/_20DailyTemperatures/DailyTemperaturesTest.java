package SecondColloquium.ColloquiumAssignments._20DailyTemperatures;

import java.io.*;
import java.util.*;

class DailyTemperatures {

    private Map<Integer, List<Double>> temperaturesMap;

    public DailyTemperatures() {
        this.temperaturesMap = new HashMap<>();
    }

    // 317 24C 29C 28C 29C
    // 140 47F 49F 46F 46F 47F 49F 48F 50F 45F 47F 46F 49F 50F 47F 50F 49F 49F 47F 45F
    public void readTemperatures(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\s+");

            int day = Integer.parseInt(parts[0]);
            List<Double> temperatures = new ArrayList<>();

            for (int i = 1; i < parts.length; i++) {
                String str = parts[i];
                if (str.endsWith("C")) {
                    temperatures.add(Double.parseDouble(str.substring(0, str.length() - 1)));
                } else if (str.endsWith("F")) {
                    double fahrenheitTemp = (Double.parseDouble(str.substring(0, str.length() - 1)));
                    double celsiusTemp = convertFromFahrenheitToCelsius(fahrenheitTemp);

                    temperatures.add(celsiusTemp);
                }
            }

            temperaturesMap.put(day, temperatures);
        }
    }

    /*
    private double parseTemperature(String temperatureCorF) {
        double value = Double.parseDouble(temperatureCorF.substring(0, temperatureCorF.length() - 1));
        char scale = temperatureCorF.charAt(temperatureCorF.length() - 1);

        return scale == 'F' ? fahrenheitToCelsius(value) : value;
    }
    */

    private double convertFromFahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }

    private double convertFromCelsiusToFahrenheit(double celsius) {
        return (celsius * 9 / 5) + 32;
    }

    // Format:
    // [ден]: Count: [вк. мерења - 3 места] Min: [мин. температура] Max: [макс. температура] Avg: [просек ]
    //    11: Count:           7            Min:        38.33C      Max:       40.56C        Avg:  39.44C
    public void writeDailyStats(OutputStream outputStream, char scale) {
        PrintWriter writer = new PrintWriter(outputStream);

        List<Integer> sortedDays = new ArrayList<>(temperaturesMap.keySet());
        Collections.sort(sortedDays);

        for (int day : sortedDays) {
            List<Double> temperatures = temperaturesMap.get(day);
            if (temperatures.isEmpty())
                continue;

            int count = temperatures.size();
            double minTemp = Collections.min(temperatures);
            double maxTemp = Collections.max(temperatures);
            double averageTemp = temperatures.stream()
                    .mapToDouble(i -> i.doubleValue())
                    .average()
                    .orElse(0.0);

            if (scale == 'F') { // Fahrenheit or Celsius
                minTemp = convertFromCelsiusToFahrenheit(minTemp);
                maxTemp = convertFromCelsiusToFahrenheit(maxTemp);
                averageTemp = convertFromCelsiusToFahrenheit(averageTemp);
            }

            writer.printf("%3d: Count: %3d Min: %6.2f%s Max: %6.2f%s Avg: %6.2f%s\n",
                    day,
                    count,
                    minTemp,
                    scale,
                    maxTemp,
                    scale,
                    averageTemp,
                    scale);
        }

        writer.flush();
    }
}

public class DailyTemperaturesTest {

    public static void main(String[] args) {

        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        try {
            dailyTemperatures.readTemperatures(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}