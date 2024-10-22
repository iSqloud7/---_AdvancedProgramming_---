package FirstColloquium.AudVezhbi05.WordCount.ReadDataWithBufferedReaderAndConsumer;

import java.util.function.Consumer;

public class LineConsumer implements Consumer<String> {

    private int characters = 0;
    private int words = 0;
    private int lines = 0;

    @Override
    public void accept(String line) {
        characters += line.length();
        words += line.split("\\s+").length;
        ++lines;
    }

    @Override
    public String toString() {
        return String.format("Characters: %d, Words: %d, Lines: %d\n", characters, words, lines);
    }
}
