package FirstColloquium.AudVezhbi04.Calculator;

public class UnknownOperatorException extends Exception {

    public UnknownOperatorException(char operator) {
        super(String.format("This operator %c is not valid.", operator));
    }
}
