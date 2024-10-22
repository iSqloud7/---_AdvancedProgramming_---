package FirstColloquium.AudVezhbi04.Calculator;

public class Calculator {

    private double result;
    private Strategy strategy;

    public Calculator() {
        this.result = 0.0;
    }

    public double getResult() {
        return result;
    }

    // Strategy Design Pattern

    public String execute(char operation, double value) throws UnknownOperatorException {
        if (operation == '+') {
//            result += value;
            strategy = new Addition();
        } else if (operation == '-') {
//            result -= value;
            strategy = new Subtraction();
        } else if (operation == '*') {
//            result *= value;
            strategy = new Multiplication();
        } else if (operation == '/') {
//            result /= value;
            strategy = new Division();
        } else {
            throw new UnknownOperatorException(operation);
        }

        result = strategy.calculate(result, value);

        return String.format("result %c %.2f = %.2f", operation, value, result);
    }

    public String init() {
        return String.format("result = %.2f", result);
    }

    @Override
    public String toString() {
        return String.format("updated result = %.2f", result);
    }
}
