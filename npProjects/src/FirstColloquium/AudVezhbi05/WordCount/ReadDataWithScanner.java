package FirstColloquium.AudVezhbi05.WordCount;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class ReadDataWithScanner {

    public static void readDataWithScanner(InputStream inputStream) {
        int characters = 0;
        int words = 0;
        int lines = 0;

        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            characters += line.length();
            words += line.split("\\s+").length;
            ++lines;
        }

        System.out.printf("Characters: %d, Words: %d, Lines: %d\n", characters, words, lines);
    }

    public static void main(String[] args) {

        // readDataFromFile
        File file = new File("C:\\Users\\Ivan\\Desktop\\NP_AudVezhbi\\AudVezhbiNP\\src\\FirstColloquium\\AudVezhbi05\\WordCount\\Input.txt");

        // FromSystemInput and FromFile
        try {
            readDataWithScanner(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
