package FirstColloquium.AudVezhbi04.Calculator;

public class Division implements Strategy {

    @Override
    public double calculate(double number1, double number2) {
        if (number2 != 0) {
            return number1 / number2;
        } else {
            return number1;
        }
    }
}
