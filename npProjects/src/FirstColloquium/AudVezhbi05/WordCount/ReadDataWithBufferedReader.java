package FirstColloquium.AudVezhbi05.WordCount;

import java.io.*;

public class ReadDataWithBufferedReader {

    public static void readDataWithBufferedReader(InputStream inputStream) throws IOException {
        int characters = 0;
        int words = 0;
        int lines = 0;

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            characters += line.length();
            words += line.split("\\s+").length;
            ++lines;
        }

        System.out.printf("Characters: %d, Words: %d, Lines: %d\n", characters, words, lines);
    }

    public static void main(String[] args) {

        File file = new File("C:\\Users\\Ivan\\Desktop\\NP_AudVezhbi\\AudVezhbiNP\\src\\FirstColloquium\\AudVezhbi05\\WordCount\\Input.txt");

        try {
            readDataWithBufferedReader(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
