package SecondColloquium.ColloquiumAssignments._29FootballTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/* Format:
  === TABLE ===
  Team                   P    W    D    L  PTS
   1. Liverpool          9    8    0    1   24
   2. Southampton       10    7    0    3   21
   3. Leicester         10    6    2    2   20
   4. Hull              10    6    2    2   20
   5. Chelsea           10    5    2    3   17
   6. Middlesbrough     10    5    1    4   16
   7. West Brom          9    5    0    4   15
   8. West Ham          10    4    2    4   14
   9. Swansea           10    4    1    5   13
  10. Everton           10    4    1    5   13
  11. Watford           10    3    3    4   12
  12. Man City           9    4    0    5   12
  13. Sunderland        10    3    3    4   12
  14. Bournemouth       10    3    2    5   11
  15. Crystal Palace    10    3    2    5   11
  16. Man Utd           10    3    2    5   11
  17. Tottenham         10    3    1    6   10
  18. Burnley           10    3    1    6   10
  19. Arsenal            9    2    3    4    9
  20. Stoke             10    3    0    7    9
*/
class Team {

    private String name;
    private int playedGames;
    private int wins;
    private int draws;
    private int losses;
    private int goalScored;
    private int goalReceived;
    /*
    public static final Comparator<Team> TEAM_COMPARATOR =
            Comparator.comparing(Team::getTotalPoints)
                    .thenComparing(Team::getGoalDifference)
                    .reversed()
                    .thenComparing(Team::getName);
    */

    public Team(String name) {
        this.name = name;
        playedGames = wins = draws = losses = goalScored = goalReceived = 0;
    }

    public String getName() {
        return name;
    }

    public int getTotalPoints() {
        return wins * 3 + draws;
    }

    public void updateStats(Game game) {
        if (name.equals(game.getHomeTeam())) { // home team
            if (game.getHomeGoals() > game.getAwayGoals()) {
                wins++;
            } else if (game.getHomeGoals() < game.getAwayGoals()) {
                losses++;
            } else {
                draws++;
            }

            goalScored += game.getHomeGoals();
            goalReceived += game.getAwayGoals();
        } else { // away team
            if (game.getHomeGoals() < game.getAwayGoals()) {
                wins++;
            } else if (game.getHomeGoals() > game.getAwayGoals()) {
                losses++;
            } else {
                draws++;
            }

            goalScored += game.getAwayGoals();
            goalReceived += game.getHomeGoals();
        }

        playedGames++;
    }

    public int getGoalDifference() {
        return goalScored - goalReceived;
    }

    @Override
    public String toString() {
        //   Team                   P    W    D    L  PTS
        //   1. Liverpool           9    8    0    1   24
        return String.format("%-15s%5d%5d%5d%5d%5d",
                getName(),
                playedGames,
                wins,
                draws,
                losses,
                getTotalPoints());
    }
}

class Game {

    private String homeTeam;
    private String awayTeam;
    private int homeGoals;
    private int awayGoals;

    public Game(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }
}

class FootballTable {

    private Map<String, Team> teamMap;

    public FootballTable() {
        this.teamMap = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        Game game = new Game(homeTeam, awayTeam, homeGoals, awayGoals);

        teamMap.putIfAbsent(homeTeam, new Team(homeTeam));
        teamMap.putIfAbsent(awayTeam, new Team(awayTeam));

        teamMap.get(homeTeam).updateStats(game);
        teamMap.get(awayTeam).updateStats(game);
    }

    public void printTable() {
        //   Team                   P    W    D    L  PTS
        //   1. Liverpool           9    8    0    1   24
        AtomicInteger ordNumber = new AtomicInteger(1);
        /*
        teamMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Team.TEAM_COMPARATOR))
                .forEach(team -> System.out.println(String.format("%2d. %s",
                        ordNumber.getAndIncrement(),
                        team)));
        */
        teamMap.values().stream()
                .sorted((t1, t2) -> {
                    int result = Integer.compare(t2.getTotalPoints(), t1.getTotalPoints());
                    if (result == 0) {
                        result = Integer.compare(t2.getGoalDifference(), t1.getGoalDifference());
                    }
                    if (result == 0) {
                        result = t1.getName().compareTo(t2.getName());
                    }

                    return result;
                })
                .forEach(team -> System.out.println(String.format("%2d. %s",
                        ordNumber.getAndIncrement(),
                        team)));

    }
}

public class FootballTableTest {

    public static void main(String[] args) throws IOException {

        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}