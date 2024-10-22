package FirstColloquium.ColloquiumAssignments._24Risk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
   X1 X2 X3;Y1 Y2 Y3,
   каде што X1, X2 и X3 се броеви добиени со фрлање на 3 коцки (број од 1-6) на напаѓачот (ATTACKER), а
            Y1, Y2 и Y3 се броеви добиени со фрлање на 3 коцки (број од 1-6) за одбрана (DEFENDER).
*/
class Round {

    private List<Integer> attacker;
    private List<Integer> defender;

    public Round(List<Integer> attacker, List<Integer> defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

    private List<Integer> parseDice(String line) {
        return Arrays.stream(line.split("\\s+"))
                .map(dice -> Integer.parseInt(dice))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public Round(String line) {
        // 5 3 4;2 4 1
        String[] parts = line.split(";");

        this.attacker = parseDice(parts[0]);
        this.defender = parseDice(parts[1]);

        /*
        this.attacker = new ArrayList<>();
        Arrays.stream(parts[0].split("\\s+"))
                .mapToInt(dice -> Integer.parseInt(dice))
                .forEach(dice -> {
                    attacker.add(dice);
                });
        */

        /*
        this.attacker = Arrays.stream(parts[0].split("\\s+"))
                .map(dice -> Integer.parseInt(dice))
                .collect(Collectors.toList());

        this.defender = Arrays.stream(parts[1].split("\\s+"))
                .map(dice -> Integer.parseInt(dice))
                .collect(Collectors.toList());
        */
    }

    public boolean hasAttackerWin() {

        /* loop
        for (int i = 0; i < attacker.size(); i++) {
            if (attacker.get(i) <= defender.get(i)) {
                return false;
            }
        }

        return true;
        */

        // stream
        return IntStream.range(0, attacker.size())
                .allMatch(i -> attacker.get(i) > defender.get(i));
    }

    @Override
    public String toString() {
        return "Round{" +
                "attacker=" + attacker +
                ", defender=" + defender +
                '}';
    }
}

class Risk {

    public int processAttacksData(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        /*
        List<Round> rounds = reader.lines()
                .map(line -> new Round(line))
                .collect(Collectors.toList());

        System.out.println(rounds);

        return (int) rounds.stream()
                .filter(round -> round.hasAttackerWin())
                .count();
        */

        int successfulAttacks = (int) reader.lines()
                .map(Round::new)
                .filter(Round::hasAttackerWin)
                .count();

        reader.close();

        return successfulAttacks;
    }
}

public class RiskTest {

    public static void main(String[] args) {

        Risk risk = new Risk();

        try {
            System.out.println(risk.processAttacksData(System.in));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}