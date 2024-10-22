package FirstColloquium.AudVezhbi04.FunctionalInterface;

public class LambdaExpressions {

    public static void main(String[] args) {

        FunctionalInterface functionalInterface1 = (x, y) -> {
            x++;
            --y;

            return x + y;
        };

        FunctionalInterface functionalInterface2 = (x, y) -> x * y;

        System.out.println("Addition: " + functionalInterface1.doOperation(5, 7));
        System.out.println("Multiplication: " + functionalInterface2.doOperation(7, 5));
    }
}
