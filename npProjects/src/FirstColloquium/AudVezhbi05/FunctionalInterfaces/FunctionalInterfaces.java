package FirstColloquium.AudVezhbi05.FunctionalInterfaces;

import java.util.Random;
import java.util.function.*;

public class FunctionalInterfaces {

    public static void main(String[] args) {

        Predicate<Integer> lessThan100 = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer < 100;
            }
        };

        Predicate<Integer> _lessThan100 = integer -> integer < 100;

        Supplier<Integer> integerSupplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return new Random().nextInt(1000);
            }
        };

        Supplier<Integer> _integerSupplier = () -> new Random().nextInt(1000);

        Consumer<String> stringConsumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };

        Consumer<String> _stringConsumer = s -> System.out.println(s);
//        Consumer<String> _stringConsumer = System.out::println; -> method reference

        Function<Integer, String> integerStringFunction = new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                return String.format("%d\n", integer);
            }
        };

        Function<Integer, String> _integerStringFunction = integer -> String.format("%d\n", integer);

        BiFunction<Integer, Integer, String> integerIntegerStringBiFunction = new BiFunction<Integer, Integer, String>() {
            @Override
            public String apply(Integer integer1, Integer integer2) {
                return String.format("%d + %d = %d\n", integer1, integer2, integer1 + integer2);
            }
        };

        BiFunction<Integer, Integer, String> _integerIntegerStringBiFunction = (x, y) -> String.format("%d + %d = %d\n", x, y, x + y);
    }
}
