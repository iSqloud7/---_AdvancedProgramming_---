package FirstColloquium.ColloquiumAssignments._11GenericFraction;

import java.util.Scanner;

class ZeroDenominatorException extends Exception {

    public ZeroDenominatorException(String message) {
        super(message);
    }
}

class GenericFraction<T extends Number, U extends Number> {

    private T numerator;
    private U denominator;

    public GenericFraction(T numerator, U denominator) throws ZeroDenominatorException {
        this.numerator = numerator;

        if (denominator.doubleValue() == 0) {
            throw new ZeroDenominatorException(String.format("Denominator cannot be zero"));
        }

        this.denominator = denominator;
    }

    public GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) {
        /*
        return new GenericFraction<>(
                this.numerator.doubleValue() * gf.denominator.doubleValue() +
                        gf.numerator.doubleValue() * this.denominator.doubleValue(),
                this.denominator.doubleValue() * gf.denominator.doubleValue());
        */

        double upper = denominator.doubleValue() * gf.denominator.doubleValue();
        double lower = numerator.doubleValue() * gf.denominator.doubleValue() +
                denominator.doubleValue() * gf.numerator.doubleValue();

        GenericFraction<Double, Double> result = null;
        try {
            result = new GenericFraction<>(upper, lower);
        } catch (ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public double toDouble() {
        return numerator.doubleValue() / denominator.doubleValue();
    }

    public double NZD(double x, double y) {
        if (y == 0) {
            return x;
        }

        return NZD(y, x % y);
    }

    @Override
    public String toString() {
        // [numerator] / [denominator], скратена (нормализирана) и секој со две децимални места.
        // 19.00 / 35.00

        double NZS = NZD(numerator.doubleValue(), denominator.doubleValue());
        return String.format("%.2f / %.2f",
                denominator.doubleValue() / NZS,
                numerator.doubleValue() / NZS);
    }
}

public class GenericFractionTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch (ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }
}