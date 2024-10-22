package FirstColloquium.ColloquiumAssignments._2ShapesApplication;

import java.io.*;
import java.util.*;

class Circle extends Shape {

    public Circle(int side, Type type) {
        super(side, Type.C);
    }

    @Override
    public double getArea() {
        return Math.PI * side * side;
    }
}

class Square extends Shape {

    public Square(int side, Type type) {
        super(side, Type.S);
    }

    @Override
    public double getArea() {
        return side * side;
    }
}

enum Type {

    C,
    S
}

abstract class Shape implements Comparable<Shape> {

    protected int side;
    private Type type;

    public Shape(int side, Type type) {
        this.side = side;
        this.type = type;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public abstract double getArea();

    @Override
    public int compareTo(Shape other) {
        return Double.compare(this.getArea(), other.getArea());
    }

    @Override
    public String toString() {
        return "Shape{" +
                "side=" + side +
                ", type=" + type +
                '}';
    }
}

class Canvas extends ShapesApplication implements Comparable<Canvas> {

    private String canvasID;
    private List<Shape> shapes;

    public Canvas(String canvasID, List<Shape> shapes) {
        super();
        this.canvasID = canvasID;
        this.shapes = shapes;
    }

    public static Canvas createCanvas(String line) throws IrregularCanvasException {
        String[] parts = line.split("\\s+"); // 0cc31e47 C 27 C 13 C 29 C 15 C 22

        String canvasID = parts[0];
        List<Shape> shapes = new ArrayList<>();

        Arrays.stream(parts)
                .skip(1)
                .forEach(i -> {
                    if (Character.isAlphabetic(i.charAt(0))) {
                        Type type = Type.valueOf(i);
                        if (type == Type.C) {
                            shapes.add(new Circle(0, type));
                        } else if (type == Type.S) {
                            shapes.add(new Square(0, type));
                        }
                    } else {
                        shapes.get(shapes.size() - 1).setSide(Integer.parseInt(i));
                    }
                });

        Canvas canvas = new Canvas(canvasID, shapes);

        if (canvas.getMAXArea() > getMaxArea()) {
            throw new IrregularCanvasException(canvasID, getMaxArea());
        }

        return canvas;
    }

    public String getCanvasID() {
        return canvasID;
    }

    public int getTotalShapes() {
        return shapes.size();
    }

    public int getTotalCircles() {
        return (int) shapes.stream()
                .filter(i -> i.getType().equals(Type.C))
                .count();
    }

    public int getTotalSquares() {
        return (int) shapes.stream()
                .filter(i -> i.getType().equals(Type.S))
                .count();
    }

    public double getMINArea() {
        return Collections.min(shapes).getArea();
    }

    public double getMAXArea() {
        return Collections.max(shapes).getArea();
    }

    public double getAverage() {
        return getCanvasArea() / getTotalShapes();
    }

    public double getCanvasArea() {
        return shapes.stream()
                .mapToDouble(Shape::getArea)
                .sum();
    }

    @Override
    public int compareTo(Canvas other) {
        return Double.compare(this.getCanvasArea(), other.getCanvasArea());
    }

    @Override
    public String toString() {
        // ID total_shapes total_circles total_squares min_area max_area average_area.

        return String.format("%s %d %d %d %.2f %.2f %.2f",
                getCanvasID(), getTotalShapes(), getTotalCircles(), getTotalSquares(), getMINArea(), getMAXArea(), getAverage());
    }
}

class IrregularCanvasException extends Exception {

    public IrregularCanvasException(String canvasID, double maxArea) {
        super(String.format("Canvas %s has a shape with area larger than %.2f",
                canvasID, maxArea));
    }
}

class ShapesApplication {

    private static final double MAX_AREA = 10000.00;
    private List<Canvas> canvases;

    public ShapesApplication() {
        this.canvases = new ArrayList<>();
    }

    public static double getMaxArea() {
        return MAX_AREA;
    }

    public void readCanvases(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        reader.lines()
                .forEach(line -> {
                    try {
                        Canvas canvas = Canvas.createCanvas(line);
                        canvases.add(canvas);
                    } catch (IrregularCanvasException e) {
                        System.out.println(e.getMessage());
                    }
                });
    }

    public void printCanvases(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);

        canvases.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(i -> writer.println(i.toString()));

        writer.flush();
    }
}

public class ShapesApplicationTest {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);
    }
}
