package FirstColloquium.ColloquiumAssignments.LineProcessor.WithBufferedReaderAndStreams;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class LineProcessor {

    private List<String> lines;

    public LineProcessor() {
        this.lines = new ArrayList<>();
    }

    public int countOcc(String line, char c) {
        /*
        int counter = 0;
        for (char character : line.toLowerCase().toCharArray()) {
            if (character == c) {
                counter++;
            }
        }

        return counter;
        */

        return (int) line.toLowerCase()
                .chars()
                .filter(i -> ((char) i == c))
                .count();
    }

    public void readLines(InputStream inputStream, OutputStream outputStream, char c) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter writer = new PrintWriter(outputStream);

        lines = reader.lines()
                .collect(Collectors.toList());

        /* Anonymous Class
        lines.stream()
                .max(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return Integer.compare(countOcc(o1, c), countOcc(o2, c));
                    }
                });
        */

        // Lambda Expressions
        Comparator<String> comparator = Comparator.comparing(string -> countOcc(string, c));

        String max = lines.stream()
                .max(comparator.thenComparing(Comparator.naturalOrder()))
                .orElse("");

        writer.println(max);

        writer.flush();
    }
}

public class LineProcessorTest {

    public static void main(String[] args) {

        LineProcessor lineProcessor = new LineProcessor();
        try {
            lineProcessor.readLines(System.in, System.out, 'a');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}