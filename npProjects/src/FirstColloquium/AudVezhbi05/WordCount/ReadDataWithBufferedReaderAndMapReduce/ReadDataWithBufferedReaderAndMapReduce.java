package FirstColloquium.AudVezhbi05.WordCount.ReadDataWithBufferedReaderAndMapReduce;

import java.io.*;

public class ReadDataWithBufferedReaderAndMapReduce {

    public static void readDataWithBufferedReaderAndMapReduce(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // reader.lines() -> stream of strings
        LineCounter LCresult = reader.lines()
                .map(l -> new LineCounter(l))
                .reduce(
                        new LineCounter(0, 0, 0), // од каде тргнуваме
                        (left, right) -> left.sum(right) // сума на меѓурезултат и нов резултат
                );

        System.out.println(LCresult);
    }

    public static void main(String[] args) {

        File file = new File("C:\\Users\\Ivan\\Desktop\\NP_AudVezhbi\\AudVezhbiNP\\src\\FirstColloquium\\AudVezhbi05\\WordCount\\Input.txt");

        try {
            readDataWithBufferedReaderAndMapReduce(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
