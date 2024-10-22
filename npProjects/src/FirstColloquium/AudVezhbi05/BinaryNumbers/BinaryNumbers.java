package FirstColloquium.AudVezhbi05.BinaryNumbers;

import java.io.*;
import java.util.Random;
import java.util.stream.IntStream;

public class BinaryNumbers {

    public static final String FILE_NAME = "C:\\Users\\Ivan\\Desktop\\NP_AudVezhbi\\AudVezhbiNP\\src\\FirstColloquium\\AudVezhbi05\\BinaryNumbers\\numbers.dat";

    public static void generateFile(int n) {
        // запишување кон бинарна датотека ObjectOutputStream
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME));

            Random random = new Random();

            // for loop итерира со i, замена со stream е IntStream, замена за тоа i
            IntStream.range(0, n)
                    .forEach(i -> {
                        int nextRandom = random.nextInt(10);
                        try {
                            objectOutputStream.writeInt(nextRandom);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            for (int i = 0; i < n; i++) {
                int nextRandom = random.nextInt(10);
                objectOutputStream.writeInt(nextRandom);
            }

            objectOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static double average() {
        int count = 0;
        double sum = 0;

        try {
            // читање од бинарна датотека ObjectInputStream
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_NAME));

            try { // рачно фатен Exception за EOF
                while (true) {
                    // ако не се прекине, пред да го прочита последниот ред ќе фрли Exception EndOfFile (EOF)
                    int number = objectInputStream.readInt();
                    sum += number;
                    count++;
                }
            } catch (EOFException e) {
                System.out.println("End of file was reached!!!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sum / count;
    }

    public static void main(String[] args) {

        generateFile(1000);

        System.out.println("Average: " + average());
    }
}
