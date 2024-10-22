package FirstColloquium.AudVezhbi04.Calculator;

public class Subtraction implements Strategy {

    @Override
    public double calculate(double number1, double number2) {
        return number1 - number2;
    }
}
