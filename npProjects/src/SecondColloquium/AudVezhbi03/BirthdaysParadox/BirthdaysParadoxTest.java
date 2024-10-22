package SecondColloquium.AudVezhbi03.BirthdaysParadox;

import java.util.HashSet;
import java.util.Random;

class BirthdayParadox {

    private int maxPeople;
    private static int TRIALS = 5000; // 10 000

    public BirthdayParadox(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    private boolean runTrial(int people, Random random) {
        HashSet<Integer> birthdays = new HashSet<>();

        for (int i = 0; i < people; i++) {
            int birthday = random.nextInt(365) + 1;
            if (birthdays.contains(birthday)) {
                return true;
            } else {
                birthdays.add(birthday);
            }
        }

        return false;
    }

    private float runSimulation(int people) {
        // 1 random објект за 1 random симулација
        Random random = new Random();
        int counter = 0;

        for (int i = 0; i < TRIALS; i++) {
            if (runTrial(people, random)) {
                counter++;
            }
        }

        return counter * 1.0f / TRIALS;
    }

    public void conductExperiment() {
        for (int i = 2; i <= maxPeople; i++) {
            System.out.println(String.format("Број на луѓе: %d --> Веројатност: %.10f%%",
                    i, runSimulation(i)));
        }
    }
}

public class BirthdaysParadoxTest {

    public static void main(String[] args) {

        BirthdayParadox birthdayParadox = new BirthdayParadox(50);

        birthdayParadox.conductExperiment();
    }
}
