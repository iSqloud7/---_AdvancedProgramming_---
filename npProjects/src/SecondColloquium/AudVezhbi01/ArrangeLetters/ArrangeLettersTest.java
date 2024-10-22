package SecondColloquium.AudVezhbi01.ArrangeLetters;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ArrangeLettersTest {

    /*
       • each word should begin with a capital letter
       • the lowercase letters should be arranged alphabetically
       • afterwards, the words in the sentence should be arranged alphabetically.
    */
    public static String arrangeWord(String word) {
        /* Добивање единствена голема буква во зборот
        char capitalLetter = word.chars()
                .mapToObj(i -> (char) i)
                .filter(character -> Character.isUpperCase(character))
                .findFirst()
                .get();
        */

        return word.chars()
                .sorted()
                .mapToObj(character -> String.valueOf((char) character))
                .collect(Collectors.joining());
    }

    public static String arrangeSentence(String sentence) {
        // 1. stream of (kO pSk sO)
        // 2. map -> stream of (Ok Skp Os)
        // 3. sorted -> stream of (Ok Os Skp) лексикографско сортирање
        // 4. collect -> joining the words with a empty space between them

        return Arrays.stream(sentence.split("\\s+"))
                .map(word -> arrangeWord(word))
                .sorted()
                .collect(Collectors.joining(" "));
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String sentence = scanner.nextLine();

        System.out.println(arrangeSentence(sentence));
    }
}
