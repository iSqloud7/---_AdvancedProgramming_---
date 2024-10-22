package SecondColloquium.AudVezhbi01.Finalists;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class InvalidPickerArgumentsException extends Exception {

    public InvalidPickerArgumentsException(String message) {
        super(message);
    }
}

class FinalistPicker {

    private int finalists;
    private static Random RANDOM = new Random(); // секогаш се генерира еден Random објект поради рамномерна распределба

    public FinalistPicker(int finalists) {
        this.finalists = finalists;
    }

    public List<Integer> pick(int n) throws InvalidPickerArgumentsException { // n -> 3
        if (n > finalists) {
            throw new InvalidPickerArgumentsException("The number n cannot exceed the number of finalists!");
        }

        if (n <= 0) {
            throw new InvalidPickerArgumentsException("The number n must be a positive number!");
        }

        List<Integer> pickedFinalists = new ArrayList<>();

        while (pickedFinalists.size() != n) {
            int pick = RANDOM.nextInt(finalists + 1); // [0, bound) + 1 -> [1, bound]
            if (!pickedFinalists.contains(pick)) {
                pickedFinalists.add(pick);
            }
        }

        return pickedFinalists;

        /* ќе има преклопување
        return RANDOM.ints(n, 1, finalists + 1)
                .boxed() // stream of integers
                .collect(Collectors.toList());
        */

        /* избегнување на преклопување со генерирање дупло повеќе броеви
        return RANDOM.ints(2 * n, 1, finalists + 1)
                .boxed() // stream of integers
                .distinct() // само уникатни броеви се задржуваат
                .limit(n) // први n броеви се задржуваат
                .collect(Collectors.toList());
        */
    }
}

public class FinalistsTest {

    public static void main(String[] args) {

        FinalistPicker picker = new FinalistPicker(3); // 3, 30

        try {
            System.out.println(picker.pick(3)); // 3, 35, -5, 0
        } catch (InvalidPickerArgumentsException e) {
            System.out.println(e.getMessage());
        }
    }
}
