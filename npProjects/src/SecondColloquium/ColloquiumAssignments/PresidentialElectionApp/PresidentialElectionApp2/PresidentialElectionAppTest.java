package SecondColloquium.ColloquiumAssignments.PresidentialElectionApp.PresidentialElectionApp2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class Candidate {

    private String candidateName;
    private int candidateVotes;

    public Candidate(String candidateName) {
        this.candidateName = candidateName;
        this.candidateVotes = 0;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public int getCandidateVotes() {
        return candidateVotes;
    }

    public int addVotes(int votes) {
        return this.candidateVotes += votes;
    }
}

class ElectionUnit {

    private int electionUnitID;
    private String electionUnitPlace;
    private Map<String, Candidate> candidatesByEU;

    public ElectionUnit(int electionUnitID, String electionUnitPlace) {
        this.electionUnitID = electionUnitID;
        this.electionUnitPlace = electionUnitPlace;
        this.candidatesByEU = new HashMap<>();
    }

    public int getElectionUnitID() {
        return electionUnitID;
    }

    public String getElectionUnitPlace() {
        return electionUnitPlace;
    }

    public void addVotes(String candidateName, int votes) {
        candidatesByEU.putIfAbsent(candidateName, new Candidate(candidateName));
        candidatesByEU.get(candidateName).addVotes(votes);
    }

    public Map<String, Integer> getVotesPerCandidate() {
        Map<String, Integer> result = new TreeMap<>();
        for (Candidate candidate : candidatesByEU.values()) {
            result.put(candidate.getCandidateName(), candidate.getCandidateVotes());
        }

        return result;
    }
}

class PresidentialElectionApp {

    private Map<Integer, ElectionUnit> electionUnits;

    public PresidentialElectionApp() {
        this.electionUnits = new HashMap<>();
    }

    // EU;EP;Candidate_1,votes_1;Candidate_2,votes_2;...;Candidate_n,votes_n
    // 1;Poll1;CandidateA,100;CandidateB,150;
    public int readData(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        int totalVotes = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");

            int electionUnitID = Integer.parseInt(parts[0]);
            String electionUnitPlace = parts[1];

            ElectionUnit electionUnit = electionUnits.putIfAbsent(electionUnitID, new ElectionUnit(electionUnitID, electionUnitPlace));
            if (electionUnit == null) {
                electionUnit = electionUnits.get(electionUnitID);
            }

            for (int i = 2; i < parts.length; i++) {
                String[] innerParts = parts[i].split(",");

                String candidateName = innerParts[0];
                int candidateVotes = Integer.parseInt(innerParts[1]);

                electionUnit.addVotes(candidateName, candidateVotes);
                totalVotes += candidateVotes;
            }
        }

        return totalVotes;
    }

    public Map<String, Integer> votesPerCandidate(Integer electionUnitID) {
        Map<String, Integer> result = new TreeMap<>();

        if (electionUnitID == null) {
            for (ElectionUnit electionUnit : electionUnits.values()) {
                electionUnit.getVotesPerCandidate()
                        .forEach((k, v) -> result.put(k, result.getOrDefault(k, 0) + v));
            }
        } else {
            ElectionUnit electionUnit = electionUnits.get(electionUnitID);
            if (electionUnit != null) {
                result.putAll(electionUnit.getVotesPerCandidate());
            }
        }

        return result;
    }
}

public class PresidentialElectionAppTest {

    public static void main(String[] args) throws IOException {

        PresidentialElectionApp app = new PresidentialElectionApp();

        System.out.println(app.readData(System.in));

        System.out.println("ALL VOTES");
        app.votesPerCandidate(null).forEach((k, v) -> System.out.printf("%s -> %d%n", k, v));

        for (int i = 1; i < 6; i++) {
            System.out.println("VOTES FOR ELECTION UNIT " + i);
            app.votesPerCandidate(i).forEach((k, v) -> System.out.printf("%s -> %d%n", k, v));
        }
    }
}