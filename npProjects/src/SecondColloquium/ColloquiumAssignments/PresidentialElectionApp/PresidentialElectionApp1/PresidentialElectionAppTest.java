package SecondColloquium.ColloquiumAssignments.PresidentialElectionApp.PresidentialElectionApp1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class PresidentialElectionApp {

    private Map<Integer, Map<String, Integer>> votesPerElectionUnit;

    public PresidentialElectionApp() {
        this.votesPerElectionUnit = new HashMap<>();
    }

    // EU;EP;Candidate_1,votes_1;Candidate_2,votes_2;...;Candidate_n,votes_n
    // 1;Poll1;CandidateA,100;CandidateB,150;
    // 2;Poll2;CandidateA,200;CandidateC,300;
    // 3;Poll3;CandidateB,50;CandidateC,100;
    public int readDate(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        int totalVotes = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");

            int electionUnit = Integer.parseInt(parts[0]);
            String electionPlace = parts[1];

            Map<String, Integer> votesPerCandidate = votesPerElectionUnit.getOrDefault(electionUnit, new HashMap<>());

            for (int i = 2; i < parts.length; i++) {
                String[] cadidateVotes = parts[i].split(",");

                String candidateName = cadidateVotes[0];
                int votes = Integer.parseInt(cadidateVotes[1]);

                if (votesPerCandidate.containsKey(candidateName)) {
                    votesPerCandidate.put(candidateName, votesPerCandidate.get(candidateName) + votes);
                } else {
                    votesPerCandidate.put(candidateName, votes);
                }

                totalVotes += votes;
            }

            votesPerElectionUnit.put(electionUnit, votesPerCandidate);
        }

        return totalVotes;
    }

    public Map<String, Integer> votesPerCandidate(Integer electionUnit) {
        Map<String, Integer> result = new TreeMap<>();

        if (electionUnit == null) {
            for (Map<String, Integer> unitVotes : votesPerElectionUnit.values()) {
                for (Map.Entry<String, Integer> entry : unitVotes.entrySet()) {
                    result.put(entry.getKey(), result.getOrDefault(entry.getKey(), 0) + entry.getValue());
                }
            }
        } else {
            Map<String, Integer> unitVotes = votesPerElectionUnit.get(electionUnit);
            if (unitVotes != null) {
                result.putAll(unitVotes);
            }
        }

        return result;
    }
}

public class PresidentialElectionAppTest {

    public static void main(String[] args) throws IOException {

        PresidentialElectionApp app = new PresidentialElectionApp();

        System.out.println(app.readDate(System.in));

        System.out.println("ALL VOTES");
        app.votesPerCandidate(null).forEach((k, v) -> System.out.printf("%s -> %d%n", k, v));

        for (int i = 1; i < 6; i++) {
            System.out.println("VOTES FOR ELECTION UNIT " + i);
            app.votesPerCandidate(i).forEach((k, v) -> System.out.printf("%s -> %d%n", k, v));
        }
    }
}