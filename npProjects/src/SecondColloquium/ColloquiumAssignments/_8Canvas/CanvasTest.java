package SecondColloquium.ColloquiumAssignments._8Canvas;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

// Shape interface
interface Shape {
    double getArea();
    double getPerimeter();
    void scale(double coefficient);
    String getUserID();
}

// Rectangle class
class Rectangle implements Shape {
    private User user;
    private double width;
    private double height;

    public Rectangle(String userID, double width, double height) {
        this.user = new User(userID);
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public String getUserID() {
        return user.getUserID();
    }

    @Override
    public double getArea() {
        return getWidth() * getHeight();
    }

    @Override
    public double getPerimeter() {
        return (getWidth() + getHeight()) * 2;
    }

    @Override
    public void scale(double coefficient) {
        width *= coefficient;
        height *= coefficient;
    }

    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f",
                getWidth(), getHeight(), getArea(), getPerimeter());
    }
}

// Square class
class Square implements Shape {
    private User user;
    private double side;

    public Square(String userID, double side) {
        this.user = new User(userID);
        this.side = side;
    }

    public double getSide() {
        return side;
    }

    @Override
    public String getUserID() {
        return user.getUserID();
    }

    @Override
    public double getArea() {
        return Math.pow(getSide(), 2);
    }

    @Override
    public double getPerimeter() {
        return 4 * getSide();
    }

    @Override
    public void scale(double coefficient) {
        side *= coefficient;
    }

    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f",
                getSide(), getArea(), getPerimeter());
    }
}

// Circle class
class Circle implements Shape {
    private User user;
    private double radius;

    public Circle(String userID, double radius) {
        this.user = new User(userID);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public String getUserID() {
        return user.getUserID();
    }

    @Override
    public double getArea() {
        return Math.pow(getRadius(), 2) * Math.PI;
    }

    @Override
    public double getPerimeter() {
        return 2 * getRadius() * Math.PI;
    }

    @Override
    public void scale(double coefficient) {
        radius *= coefficient;
    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f",
                getRadius(), getArea(), getPerimeter());
    }
}

// User class
class User {
    private String userID;

    public User(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
}

// Exception for invalid user ID
class InvalidIDException extends Exception {
    public InvalidIDException(String userID) {
        super(String.format("ID %s is not valid", userID));
    }
}

// Exception for invalid dimensions
class InvalidDimensionException extends Exception {
    public InvalidDimensionException(String message) {
        super(message);
    }
}

// Canvas class
class Canvas {
    private List<Shape> shapes;

    public Canvas() {
        this.shapes = new ArrayList<>();
    }

    private boolean isValidUserID(String userID) {
        return userID.length() == 6 && userID.chars().allMatch(Character::isLetterOrDigit);
    }

    public void readShapes(InputStream is) throws IOException, InvalidIDException, InvalidDimensionException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\s+");
            if (parts.length < 3) continue;
            int shapeType = Integer.parseInt(parts[0]);
            String userID = parts[1];
            if (!isValidUserID(userID)) {
                System.out.println(new InvalidIDException(userID).getMessage());
                continue;
            }

            Shape shape;
            try {
                switch (shapeType) {
                    case 1:
                        double radius = Double.parseDouble(parts[2]);
                        if (radius == 0) {
                            throw new InvalidDimensionException("Dimension 0 is not allowed!");
                        }
                        shape = new Circle(userID, radius);
                        break;
                    case 2:
                        double side = Double.parseDouble(parts[2]);
                        if (side == 0) {
                            throw new InvalidDimensionException("Dimension 0 is not allowed!");
                        }
                        shape = new Square(userID, side);
                        break;
                    case 3:
                        double width = Double.parseDouble(parts[2]);
                        double height = Double.parseDouble(parts[3]);
                        if (width == 0 && height == 0) {
                            throw new InvalidDimensionException("Dimension 0 is not allowed!");
                        }
                        shape = new Rectangle(userID, width, height);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown shape type: " + shapeType);
                }
                shapes.add(shape);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid input line: " + line);
            }
        }
    }

    public void scaleShapes(String userID, double coef) {
        for (Shape shape : shapes) {
            if (shape.getUserID().equals(userID)) {
                shape.scale(coef);
            }
        }
    }

    public void printAllShapes(OutputStream os) {
        PrintWriter writer = new PrintWriter(os);
        List<Shape> sortedShapes = shapes.stream()
                .sorted(Comparator.comparing(Shape::getArea)
                        .thenComparing(Shape::getPerimeter))
                .collect(Collectors.toList());

        sortedShapes.forEach(shape -> writer.println(shape.toString()));
        writer.flush();
    }

    public void printByUserId(OutputStream os) {
        PrintWriter writer = new PrintWriter(os);
        Map<String, List<Shape>> shapesByUserID = shapes.stream()
                .collect(Collectors.groupingBy(Shape::getUserID));

        List<String> sortedUsers = shapesByUserID.keySet().stream()
                .sorted(Comparator.comparing((String userID) -> shapesByUserID.get(userID).size())
                        .reversed()
                        .thenComparing(userID -> shapesByUserID.get(userID).stream()
                                        .mapToDouble(Shape::getArea)
                                        .sum(),
                                Comparator.reverseOrder()))
                .collect(Collectors.toList());

        for (String userID : sortedUsers) {
            writer.printf("Shapes of user: %s\n", userID);
            List<Shape> shapeList = shapesByUserID.get(userID).stream()
                    .sorted(Comparator.comparing(Shape::getPerimeter)
                            .reversed())
                    .collect(Collectors.toList());

            shapeList.forEach(shape -> writer.println(shape.toString()));
        }
        writer.flush();
    }

    public void statistics(OutputStream os) {
        PrintWriter writer = new PrintWriter(os);
        DoubleSummaryStatistics statistics = shapes.stream()
                .mapToDouble(Shape::getArea)
                .summaryStatistics();

        writer.printf("count: %d\nsum: %.2f\nmin: %.2f\naverage: %.2f\nmax: %.2f\n",
                statistics.getCount(), statistics.getSum(), statistics.getMin(), statistics.getAverage(), statistics.getMax());

        writer.flush();
    }
}

// Test class for Canvas
public class CanvasTest {
    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        try {
            canvas.readShapes(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidIDException | InvalidDimensionException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}
