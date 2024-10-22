package FirstColloquium.AudVezhbi06.StandardDeviation;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class GenericMathOperationsTest {

    public static String statistics(List<? extends Number> numbers) {
        /*
        DoubleSummaryStatistics doubleSummaryStatistics = new DoubleSummaryStatistics();

        numbers.stream()
                .forEach(i -> doubleSummaryStatistics.accept(i.doubleValue()));
        */

        DoubleSummaryStatistics doubleSummaryStatistics = numbers.stream()
                .mapToDouble(i -> i.doubleValue())
                .summaryStatistics();

        double standardDeviation = 0;

        // во stream не е дозволено внатре да се менува нешто кое е од надвор дефинирано
        // stream може само во сопствениот тек да менува
        /*
        numbers.stream()
                .forEach(i -> {
                    standardDeviation += 1;
                });
        */

        for (Number number : numbers) {
            standardDeviation += (number.doubleValue() - doubleSummaryStatistics.getAverage()) *
                    (number.doubleValue() - doubleSummaryStatistics.getAverage());
        }

        double finalStandardDeviation = Math.sqrt(standardDeviation / numbers.size());

        return String.format("Min: %.4f\nMax: %.2f\nAverage: %.2f\nStandard Deviation: %.2f\n" + "Count: %d\nSum: %.2f",
                doubleSummaryStatistics.getMin(),
                doubleSummaryStatistics.getMax(),
                doubleSummaryStatistics.getAverage(),
                standardDeviation,
                doubleSummaryStatistics.getCount(),
                doubleSummaryStatistics.getSum());
    }

    public static void main(String[] args) {

        Random random = new Random();

        List<Integer> integers = new ArrayList<>();

        IntStream.range(0, 100000)
                .forEach(i -> integers.add(random.nextInt(100) + 1));

        /*
        for (int i = 0; i < 100000; i++) {
            integers.add(random.nextInt(100) + 1);
        }
        */

        List<Double> doubles = new ArrayList<>();

        IntStream.range(0, 100000)
                .forEach(i -> doubles.add(random.nextDouble() * 100.0));

        /*
        for (int i = 0; i < 100000; i++) {
            doubles.add(random.nextDouble() * 100.0);
        }
        */

        System.out.println(statistics(integers));
        System.out.println(statistics(doubles));
    }
}
