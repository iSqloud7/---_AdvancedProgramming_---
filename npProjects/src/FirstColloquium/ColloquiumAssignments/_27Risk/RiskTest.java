package FirstColloquium.ColloquiumAssignments._27Risk;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Round {

    private List<Integer> attacker;
    private List<Integer> defender;

    public Round(List<Integer> attacker, List<Integer> defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

    public List<Integer> getAttacker() {
        return attacker;
    }

    public List<Integer> getDefender() {
        return defender;
    }

    public Round(String line) {
        // 5 3 4;2 4 1
        String[] parts = line.split(";");

        attacker = parseDice(parts[0]);
        defender = parseDice(parts[1]);
    }

    public List<Integer> parseDice(String line) {
        return Arrays.stream(line.split("\\s+"))
                .map(dice -> Integer.parseInt(dice))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }
}

class Risk {

    public void processAttacksData(InputStream is) {
        Scanner scanner = new Scanner(is);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Round round = new Round(line);
            int attackerWins = 0;
            int defenderWins = 0;
            for (int i = 0; i < 3; i++) {
                if (round.getAttacker().get(i) > round.getDefender().get(i)) {
                    attackerWins++;
                } else {
                    defenderWins++;
                }
            }

            System.out.println(attackerWins + " " + defenderWins);
        }
    }
}

public class RiskTest {

    public static void main(String[] args) {

        Risk risk = new Risk();
        risk.processAttacksData(System.in);
    }
}