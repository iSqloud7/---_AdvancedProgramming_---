package FirstColloquium.AudVezhbi04.FunctionalInterface;

public class AnonymousClass {

    // секоја анонимна класа мора да имплементира интерфејс\
    FunctionalInterface Addition = new FunctionalInterface() {
        @Override
        public double doOperation(double a, double b) {
            return a + b;
        }
    };

    public static void main(String[] args) {

        AnonymousClass anonymousClass = new AnonymousClass();
        System.out.println(anonymousClass.Addition.doOperation(5, 5));
    }
}
