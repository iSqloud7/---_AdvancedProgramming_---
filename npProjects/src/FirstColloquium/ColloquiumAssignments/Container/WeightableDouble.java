package FirstColloquium.ColloquiumAssignments.Container;

public class WeightableDouble implements Weightable {

    private double weight;

    public WeightableDouble(double weight) {
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }
}
