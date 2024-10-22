package FirstColloquium.ColloquiumAssignments.Container;

public interface Weightable extends Comparable<Weightable> { // interface extends other interface

    public double getWeight();

    @Override
    default int compareTo(Weightable other) {
        return Double.compare(this.getWeight(), other.getWeight());
    }
}
