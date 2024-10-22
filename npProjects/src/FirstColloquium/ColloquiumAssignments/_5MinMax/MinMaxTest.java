package FirstColloquium.ColloquiumAssignments._5MinMax;

import java.util.Scanner;

class MinMax<T extends Comparable<T>> {

    private T min;
    private T max;
    private int updateCount;
    private int processedMIN;
    private int processedMAX;

    public MinMax() {
        this.updateCount = 0;
        this.processedMIN = 0;
        this.processedMAX = 0;
    }

    public void update(T element) {
        if (updateCount == 0) {
            min = element;
            max = element;
        }
        if (min.compareTo(element) > 0) {
            min = element;
            processedMIN = 1;
        } else if (min.compareTo(element) == 0) {
            processedMIN++;
        }
        if (max.compareTo(element) < 0) {
            max = element;
            processedMAX = 1;
        } else if (max.compareTo(element) == 0) {
            processedMAX++;
        }

        updateCount++;
    }

    public T max() {
        return max;
    }

    public T min() {
        return min;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d\n",
                min, max, (updateCount - processedMIN - processedMAX));
    }
}

public class MinMaxTest {

    public static void main(String[] args) throws ClassNotFoundException {

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for (int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}