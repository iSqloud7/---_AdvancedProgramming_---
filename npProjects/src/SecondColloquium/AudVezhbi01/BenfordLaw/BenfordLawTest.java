package SecondColloquium.AudVezhbi01.BenfordLaw;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Counter {

    private int countingArray[];

    public Counter() {
        this.countingArray = new int[10];
    }

    public void addToCounter(int digit) {
        countingArray[digit]++;
    }

    @Override
    public String toString() {
        // StringBuilder builder = new StringBuilder();

        // sum with stream
        int sum = Arrays.stream(countingArray).sum();

        /* sum with for loop
        int sum = 0;
        for (int i = 0; i < countingArray.length; i++) {
            sum += countingArray[i];
        }
        */

        // with IntStream
        return IntStream.range(0, 10)
                .mapToObj(i -> String.format("[%d: %.2f%%]",
                        i, countingArray[i] * 100.0 / sum))
                .collect(Collectors.joining("\n"));

        /* with loop and StringBuilder
        for (int i = 0; i < countingArray.length; i++) { // or i < 10
            // builder.append(String.format("[%d: %d]", i, countingArray[i])).append("\n");

            // in %
            builder.append(String.format("[%d: %.2f%%]",
                            i, countingArray[i] * 100.0 / sum))
                    .append("\n");
        }

        return builder.toString();
        */
    }
}

class BenfordLawExperiment {

    private List<Integer> numbers;
    private Counter counter;

    public BenfordLawExperiment() {
        this.numbers = new ArrayList<>();
        this.counter = new Counter();
    }

    public void readData(InputStream inputStream) {
        /* Scanner
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNext()) {
            int number = scanner.nextInt();

            numbers.add(number);
        }
        */

        // BufferedReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        numbers = reader.lines() // stream of strings
                .filter(line -> !line.equals("")) // проверка во случај ако има празен string
                .map(line -> Integer.parseInt(line))
                .collect(Collectors.toList());
    }

    private static int getFirstDigit(int number) {
        while (number >= 10) {
            number /= 10;
        }

        return number;
    }

    public void conductExperiment() {
        numbers.stream()
                .map(number -> getFirstDigit(number))
                .forEach(firstDigit -> counter.addToCounter(firstDigit));
    }

    @Override
    public String toString() {
        return counter.toString();
    }
}

public class BenfordLawTest {

    /*
    private static int getFirstDigit(int number) {
        while (number >= 10) {
            number /= 10;
        }

        return number;
    }
    */

    public static void main(String[] args) {

        BenfordLawExperiment experiment = new BenfordLawExperiment();
        // System.out.println(getFirstDigit(123456789)); // проверка дали ја зема првата цифра од бројот

        try {
            experiment.readData(new FileInputStream("C:\\Users\\Ivan\\Desktop\\NP_AudVezhbi\\AudVezhbiNP\\src\\SecondColloquium\\AudVezhbi01\\BenfordLaw\\ListOfNumbers.txt"));
            // System.out.println(experiment);
            experiment.conductExperiment();
            System.out.println(experiment);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /*
        Counter counter = new Counter();

        System.out.println(counter);
        */
    }
}
