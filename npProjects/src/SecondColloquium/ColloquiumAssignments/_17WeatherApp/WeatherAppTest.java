package SecondColloquium.ColloquiumAssignments._17WeatherApp;

import java.util.*;

interface IObserver {

    void update();

    int priority();
}

abstract class Observer implements IObserver {

    WeatherDispatcher dispatcher;

    public Observer(WeatherDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        dispatcher.register(this);
    }
}

/* Format:
   Forecast: [Improving, Same, Cooler],
   Improving се печати доколку моменталниот притисок е поголем од претходно прикажаниот.
   Same се печати доколку моменталниот притисок е еднаков на претходно прикажаниот.
   Cooler се печати доколку моменталниот притисок е помал од претходно прикажаниот.
   Првичниот притисок е поставен на вредност 0.0.
*/
class ForecastDisplay extends Observer {

    private float lastPressure;


    public ForecastDisplay(WeatherDispatcher dispatcher) {
        super(dispatcher);
        this.lastPressure = dispatcher.getPressure();
    }

    @Override
    public void update() {
        // Forecast: [Improving, Same, Cooler]
        System.out.printf("Forecast: ");
        if (dispatcher.getPressure() > lastPressure) {
            System.out.println("Improving");
        } else if (dispatcher.getPressure() == lastPressure) {
            System.out.println("Same");
        } else { // dispatcher.getPressure() < lastPressure
            System.out.println("Cooler");
        }

        lastPressure = dispatcher.getPressure();
    }

    @Override
    public int priority() {
        return 1;
    }
}

/* Format:
   За CurrentConditionsDisplay форматот е -
   Temperature: [тековна температура]F
   Humidity: [тековна влажност]%
*/
class CurrentConditionsDisplay extends Observer {

    public CurrentConditionsDisplay(WeatherDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void update() {
        /* Format:
           Temperature: 1.0F
           Humidity: 2.0%
           Forecast: Improving
        */
        System.out.printf("Temperature: %.1fF\n",
                dispatcher.getTemperature());
        System.out.printf("Humidity: %.1f%%\n",
                dispatcher.getHumidity());
    }

    @Override
    public int priority() {
        return 0;
    }
}

class WeatherDispatcher {

    private float temperature;
    private float humidity;
    private float pressure;
    private Set<IObserver> observers;

    public WeatherDispatcher() {
        this.temperature = 0.0F;
        this.humidity = 0.0F;
        this.pressure = 0.0F;
        this.observers = new TreeSet<>(Comparator.comparing(IObserver::priority));
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        notifyDisplays();
        System.out.println();
    }

    public void register(IObserver observer) {
        observers.add(observer);
    }

    public void remove(IObserver observer) {
        observers.remove(observer);
    }

    private void notifyDisplays() {
        observers.stream()
                .forEach(observer -> observer.update());
    }
}

public class WeatherAppTest {

    public static void main(String[] args) {

        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if (parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if (operation == 1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if (operation == 2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if (operation == 3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if (operation == 4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}