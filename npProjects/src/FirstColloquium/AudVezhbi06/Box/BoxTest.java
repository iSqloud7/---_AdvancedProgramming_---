package FirstColloquium.AudVezhbi06.Box;

import java.util.stream.IntStream;

public class BoxTest {

    public static void main(String[] args) {

        /*
        Box<Integer> box = new Box<Integer>();
        // stream of integers -> IntStream
        IntStream.range(0, 100)
                .forEach(i -> box.addElement(i)); // or box::add -> method reference, bcs there is only one such method in that class

        IntStream.range(0, 105)
                .forEach(el -> System.out.println(box.drawItem()));
        */

        Box<Circle> circleBox = new Box<Circle>();

        IntStream.range(0, 100)
                .forEach(i -> new Circle());

        IntStream.range(0, 105)
                .forEach(el -> System.out.println(circleBox.drawItem()));
    }
}
