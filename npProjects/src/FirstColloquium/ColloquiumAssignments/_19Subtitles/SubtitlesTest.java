package FirstColloquium.ColloquiumAssignments._19Subtitles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class SubtitleElement {

    private int index;
    protected long startTime;
    protected long endTime;
    List<String> text;

    public SubtitleElement(int index, long startTime, long endTime, List<String> text) {
        this.index = index;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = text;
    }

    public int getIndex() {
        return index;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public List<String> getText() {
        return text;
    }

    public String formatTime(long time) {
        // 00:00:48,321
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss,SSS");

        return simpleDateFormat.format(time);
    }

    @Override
    public String toString() {
        /*
           2
           00:00:48,321 --> 00:00:50,837
           Let's see a real bet.
        */

        StringBuilder builder = new StringBuilder();
        builder.append(getIndex()) // 2
                .append("\n");
        builder.append(formatTime(startTime)) // 00:00:48,321
                .append(" --> ") //  -->
                .append(formatTime(endTime)) // 00:00:50,837
                .append("\n");
        for (String line : text) {
            builder.append(line)
                    .append("\n");
        }

        return builder.toString();
    }
}

class Subtitles {

    private List<SubtitleElement> elements;

    public Subtitles() {
        this.elements = new ArrayList<>();
    }

    /*
       2
       00:00:48,321 --> 00:00:50,837
       Let's see a real bet.
    */
    public int loadSubtitles(InputStream inputStream) throws ParseException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss,SSS");
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                int index = Integer.parseInt(line);

                String timeLine = reader.readLine();

                String[] times = timeLine.split(" --> ");
                long startTime = simpleDateFormat.parse(times[0]).getTime();
                long endTime = simpleDateFormat.parse(times[1]).getTime();

                List<String> textLines = new ArrayList<>();
                while ((line = reader.readLine()) != null && !(line.trim().isEmpty())) {
                    textLines.add(line);
                }

                elements.add(new SubtitleElement(index, startTime, endTime, textLines));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        int countLinesRead = (int) elements.stream()
                .count();

        return countLinesRead;
    }

    public void print() {
        for (SubtitleElement element : elements) {
            System.out.println(element);
        }
    }

    public void shift(int ms) {
        for (SubtitleElement element : elements) {
            element.startTime += ms;
            element.endTime += ms;
        }
    }
}

public class SubtitlesTest {

    public static void main(String[] args) {

        Subtitles subtitles = new Subtitles();
        int n = 0;
        try {
            n = subtitles.loadSubtitles(System.in);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}