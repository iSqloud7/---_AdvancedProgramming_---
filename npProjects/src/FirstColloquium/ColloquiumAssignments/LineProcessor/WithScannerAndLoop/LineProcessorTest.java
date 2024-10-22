package FirstColloquium.ColloquiumAssignments.LineProcessor.WithScannerAndLoop;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Line implements Comparable<Line> {

    private String line;
    private char c;

    public Line(String line, char c) {
        this.line = line;
        this.c = c;
    }

    private int countOcc() {
        int counter = 0;

        for (char character : line.toLowerCase().toCharArray()) {
            if (character == this.c) {
                counter++;
            }
        }

        return counter;
    }

    @Override
    public int compareTo(Line other) {
        return Integer.compare(this.countOcc(), other.countOcc());
    }

    @Override
    public String toString() {
        return line;
    }
}

class LineProcessor {

    private List<Line> lines;

    public LineProcessor() {
        this.lines = new ArrayList<>();
    }

    public void readLines(InputStream inputStream, OutputStream outputStream, char c) {
        Scanner scanner = new Scanner(inputStream);
        PrintWriter writer = new PrintWriter(outputStream);

        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();

            Line lineObj = new Line(line, c);
            lines.add(lineObj);
        }

        scanner.close();

        Line max = lines.get(0);
        for (Line l : lines) {
            if (l.compareTo(max) >= 0) {
                max = l;
            }
        }

        writer.println(max);

        writer.flush();
    }
}

public class LineProcessorTest {

    public static void main(String[] args) {

        LineProcessor lineProcessor = new LineProcessor();

        lineProcessor.readLines(System.in, System.out, 'a');
    }
}