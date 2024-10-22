package FirstColloquium.ColloquiumAssignments._1ShapesApplication;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Square {

    private int side;

    public Square(int side) {
        this.side = side;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }
}

class Canvas implements Comparable<Canvas> {

    private String canvasID;
    protected List<Square> squares;

    public Canvas() {
        this.canvasID = "";
        this.squares = new ArrayList<>();
    }

    public Canvas(String canvasID, List<Square> squares) {
        this.canvasID = canvasID;
        this.squares = squares;
    }

    public static Canvas createCanvas(String line) {
        String[] parts = line.split("\\s+");

        // 364fbe94 24 30 22 33 32 30 37 18 29 27 33 21 27 26

        String canvasID = parts[0];

        /* итеративно програмирање
        List<Square> squares = new ArrayList<>();

        for (int i = 1; i < parts.length; i++) {
            squares.add(new Square(Integer.parseInt(parts[i]))));
        }

        return new Canvas(canvasID, squares);
        */

        // with streams
        List<Square> squares = new ArrayList<>();

        Arrays.stream(parts)
                .skip(1)
                .map(i -> squares.add(new Square(Integer.parseInt(i))))
                .collect(Collectors.toList());

        return new Canvas(canvasID, squares);
    }

    private int totalSquaresPerimeter() {
        return squares.stream()
                .mapToInt(i -> i.getSide() * 4)
                .sum();
    }

    @Override
    public String toString() {
        // canvas_id squares_count total_squares_perimeter

        return String.format("%s %d %d",
                canvasID, squares.size(), totalSquaresPerimeter());
    }

    @Override
    public int compareTo(Canvas other) {
        return Integer.compare(this.totalSquaresPerimeter(), other.totalSquaresPerimeter());
    }
}

class ShapesApplication {

    private List<Canvas> canvases;

    public int readCanvases(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


        canvases = reader.lines()
                .map(line -> Canvas.createCanvas(line))
                .collect(Collectors.toList());

        // return canvases.size(); // returns how many canvases

        return canvases.stream()
                .mapToInt(canvas -> canvas.squares.size())
                .sum();
    }

    public void printLargestCanvasTo(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);

        Canvas largestCanvas = canvases.stream()
                .max(Comparator.naturalOrder())
                .get();

        writer.println(largestCanvas.toString());

        writer.flush();
    }
}

public class ShapesApplicationTest {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
