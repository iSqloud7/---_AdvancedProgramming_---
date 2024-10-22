package FirstColloquium.AudVezhbi05.WordCount.ReadDataWithBufferedReaderAndMapReduce;

public class LineCounter {

    private int characters;
    private int words;
    private int lines;

    public LineCounter(int characters, int words, int lines) {
        this.characters = characters;
        this.words = words;
        this.lines = lines;
    }

    public LineCounter(String line) {
        characters = line.length();
        words = line.split("\\s+").length;
        ++lines;
    }

    public LineCounter sum(LineCounter otherLC) {
        return new LineCounter(this.characters + otherLC.characters,
                this.words + otherLC.words,
                this.lines + otherLC.lines);
    }

    @Override
    public String toString() {
        return String.format("Characters: %d, Words: %d, Lines: %d\n", characters, words, lines);
    }
}
