package FirstColloquium.ColloquiumAssignments.Container;

public class WeightableString implements Weightable {

    private String word;

    public WeightableString(String word) {
        this.word = word;
    }

    @Override
    public double getWeight() {
        return word.length();
    }
}
