// Package declaration
package FirstColloquium.ColloquiumAssignments.LineProcessor.WithScannerAndLoop.WithComments;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Represents a line of text and its associated character to count occurrences
class Line implements Comparable<Line> {

    // The line of text
    private String line;

    // The character to count in the line
    private char c;

    // Constructor initializes the line and the character to count
    public Line(String line, char c) {
        this.line = line;
        this.c = c;
    }

    // Method to count the occurrences of the character `c` in the line
    private int countOcc() {
        int counter = 0;

        // Convert the line to lowercase and iterate through each character
        for (char character : line.toLowerCase().toCharArray()) {
            // Increment counter if the character matches `c`
            if (character == this.c) {
                counter++;
            }
        }

        // Return the total count
        return counter;
    }

    // Method to compare the number of occurrences of `c` in this line with another line
    @Override
    public int compareTo(Line other) {
        // Compare based on the count of occurrences
        return Integer.compare(this.countOcc(), other.countOcc());
    }

    // Method to return the line as a string
    @Override
    public String toString() {
        return line;
    }
}

// Class to process multiple lines of text
class LineProcessor {

    // List to store all lines
    private List<Line> lines;

    // Constructor initializes the list of lines
    public LineProcessor() {
        this.lines = new ArrayList<>();
    }

    // Method to read lines from an input stream, process them, and write the result to an output stream
    public void readLines(InputStream inputStream, OutputStream outputStream, char c) {
        // Scanner to read input
        Scanner scanner = new Scanner(inputStream);
        // PrintWriter to write output
        PrintWriter writer = new PrintWriter(outputStream);

        String line;
        // Read lines from the input stream
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();

            // Create a new Line object and add it to the list
            Line lineObj = new Line(line, c);
            lines.add(lineObj);
        }

        // Close the scanner after reading input
        scanner.close();

        // Initialize the maximum occurrence line as the first line
        Line max = lines.get(0);
        // Iterate through the list to find the line with the maximum occurrences of `c`
        for (Line l : lines) {
            if (l.compareTo(max) >= 0) {
                max = l;
            }
        }

        // Write the line with the maximum occurrences to the output stream
        writer.println(max);

        // Flush the writer to ensure the output is written
        writer.flush();
    }
}

// Test class with a main method to run the LineProcessor
public class LineProcessorTest {

    public static void main(String[] args) {
        // Create a new LineProcessor object
        LineProcessor lineProcessor = new LineProcessor();

        // Call the readLines method to process input from the console and output to the console
        lineProcessor.readLines(System.in, System.out, 'a');
    }
}