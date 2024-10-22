package FirstColloquium.AudVezhbi02.StringPrefix;

import java.util.stream.IntStream;

public class StringPrefixWithStreams {

    public static boolean isPrefix(String string1, String string2) {
        if (string1.length() > string2.length()) {
            return false;
        }

        return IntStream.range(0, string1.length())
                .allMatch(i -> string1.charAt(i) == string2.charAt(i));
    }

    public static void main(String[] args) {

        System.out.println("Is prefix? " + isPrefix("napredno_programiranje", "np"));
        // StringPrefix.isPrefix() -> there is no need bcs they are in the same class
        System.out.println("Is predix? " + StringPrefix.isPrefix("ivan", "ivanP"));
    }
}
