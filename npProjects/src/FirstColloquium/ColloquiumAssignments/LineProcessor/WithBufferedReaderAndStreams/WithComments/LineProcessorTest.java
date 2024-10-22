// Package declaration
package FirstColloquium.ColloquiumAssignments.LineProcessor.WithBufferedReaderAndStreams.WithComments;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Class to process lines using BufferedReader and Java Streams
class LineProcessor {

    // List to store all lines
    private List<String> lines;

    // Constructor initializes the list of lines
    public LineProcessor() {
        this.lines = new ArrayList<>();
    }

    // Method to count the occurrences of a character `c` in a given line
    public int countOcc(String line, char c) {
        /* Original implementation using a loop to count occurrences of `c`
        int counter = 0;

        // Convert the line to lowercase and iterate through each character
        for (char character : line.toLowerCase().toCharArray()) {
            // Increment counter if the character matches `c`
            if (character == c) {
                counter++;
            }
        }

        // Return the total count
        return counter;
        */

        // Stream-based implementation to count occurrences of `c`
        return (int) line.toLowerCase() // Convert the line to lowercase
                .chars()                // Convert the string to an IntStream of character codes
                .filter(i -> ((char) i == c)) // Filter the stream to include only occurrences of `c`
                .count();               // Count the number of occurrences and cast the result to int
    }

    // Method to read lines from an input stream, process them, and write the result to an output stream
    public void readLines(InputStream inputStream, OutputStream outputStream, char c) throws IOException {
        // BufferedReader to read input more efficiently
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        // PrintWriter to write output
        PrintWriter writer = new PrintWriter(outputStream);

        // Read all lines from the input stream and collect them into the list
        lines = reader.lines()
                .collect(Collectors.toList());

        /*
         * Alternative method using an anonymous class to find the line with the maximum occurrences
         * of the character `c`.
         *
         * lines.stream()
         *     .max(new Comparator<String>() {
         *         @Override
         *         public int compare(String o1, String o2) {
         *             return Integer.compare(countOcc(o1, c), countOcc(o2, c));
         *         }
         *     });
         */

        // Lambda expression to create a comparator based on the count of occurrences of `c`
        Comparator<String> comparator = Comparator.comparing(string -> countOcc(string, c));

        // Stream the lines, find the maximum based on the comparator, and resolve ties using natural order
        String max = lines.stream()
                .max(comparator.thenComparing(Comparator.naturalOrder()))
                .orElse(""); // Default to an empty string if no lines are found

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
        try {
            // Call the readLines method to process input from the console and output to the console
            lineProcessor.readLines(System.in, System.out, 'a');
        } catch (IOException e) {
            // Handle any IOExceptions that occur during the process
            e.printStackTrace();
        }
    }
}