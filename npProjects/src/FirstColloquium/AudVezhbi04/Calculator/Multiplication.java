package FirstColloquium.AudVezhbi04.Calculator;

public class Multiplication implements Strategy {

    @Override
    public double calculate(double number1, double number2) {
        return number1 * number2;
    }
}
