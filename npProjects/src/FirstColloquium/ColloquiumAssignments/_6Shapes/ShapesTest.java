package FirstColloquium.ColloquiumAssignments._6Shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum Color {

    RED,
    GREEN,
    BLUE
}

interface Scalable {

    void scale(float scaleFactor);
}

interface Stackable {

    float weight();
}

class Circle extends Shape {

    private float radius;

    public Circle(String canvasID, Color color, float radius) {
        super(canvasID, color);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public void scale(float scaleFactor) {
        radius *= scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (Math.PI * radius * radius);
    }

    @Override
    public String toString() {
        // C: [id:5 места од лево] [color:10 места од десно] [weight:10.2 места од десно]
        return String.format("C: %-5s%-10s%10.2f",
                getShapeID(), getColor(), weight());
    }
}

class Rectangle extends Shape {

    private float width;
    private float height;

    public Rectangle(String canvasID, Color color, float width, float height) {
        super(canvasID, color);
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public void scale(float scaleFactor) {
        width *= scaleFactor;
        height *= scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }

    @Override
    public String toString() {
        // R: [id:5 места од лево] [color:10 места од десно] [weight:10.2 места од десно]
        return String.format("R: %-5s%-10s%10.2f",
                getShapeID(), getColor(), weight());
    }
}

abstract class Shape implements Scalable, Stackable {

    private String shapeID;
    private Color color;

    public Shape(String shapeID, Color color) {
        this.shapeID = shapeID;
        this.color = color;
    }

    public String getShapeID() {
        return shapeID;
    }

    public Color getColor() {
        return color;
    }
}

class Canvas {

    private List<Shape> shapes;

    public Canvas() {
        this.shapes = new ArrayList<>();
    }

    private int findInsertionIndex(float weight) {
        /*
        return IntStream.range(0, shapes.size())
                .filter(i -> shapes.get(i).weight() < weight)
                .findFirst()
                .orElse(shapes.size());
         */

        for (int i = 0; i < shapes.size(); i++) {
            if (shapes.get(i).weight() < weight) {
                return i;
            }
        }

        return shapes.size();
    }

    public void add(String ID, Color color, float radius) {
        Circle circle = new Circle(ID, color, radius);

        int index = findInsertionIndex(circle.weight());

        shapes.add(index, circle);
    }

    public void add(String ID, Color color, float width, float height) {
        Rectangle rectangle = new Rectangle(ID, color, width, height);

        int index = findInsertionIndex(rectangle.weight());

        shapes.add(index, rectangle);
    }

    public void scale(String ID, float scaleFactor) {
        Shape shape = null;

        for (int i = shapes.size() - 1; i >= 0; i--) {
            if (shapes.get(i).getShapeID().equals(ID)) {
                shape = shapes.get(i);
                shapes.remove(i);
                break;
            }
        }

        if (shape != null) {
            shape.scale(scaleFactor);
            int index = findInsertionIndex(shape.weight());
            shapes.add(index, shape);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Shape shape : shapes) {
            builder.append(shape)
                    .append("\n");
        }

        return builder.toString();
    }
}

public class ShapesTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }
        }
    }
}