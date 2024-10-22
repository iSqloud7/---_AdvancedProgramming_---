package FirstColloquium.AudVezhbi05.WordCount.ReadDataWithBufferedReaderAndConsumer;

import java.io.*;

public class ReadDataWithBufferedReaderAndConsumer {

    public static void readDataWithBufferedReaderAndConsumer(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        LineConsumer lineConsumer = new LineConsumer();

        reader.lines()
                .forEach(lineConsumer);

        System.out.println(lineConsumer);
    }

    public static void main(String[] args) {

        File file = new File("C:\\Users\\Ivan\\Desktop\\NP_AudVezhbi\\AudVezhbiNP\\src\\FirstColloquium\\AudVezhbi05\\WordCount\\Input.txt");

        try {
            readDataWithBufferedReaderAndConsumer(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
