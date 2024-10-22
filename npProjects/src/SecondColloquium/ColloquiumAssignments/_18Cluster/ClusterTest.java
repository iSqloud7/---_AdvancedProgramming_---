package SecondColloquium.ColloquiumAssignments._18Cluster;

import java.util.*;
import java.util.stream.Collectors;

class Point2D {

    private long ID;
    private float x;
    private float y;

    public Point2D(long ID, float x, float y) {
        this.ID = ID;
        this.x = x;
        this.y = y;
    }

    public long getID() {
        return ID;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    // Евклидово растојание $\sqrt{{(x1 - x2)^2} + {(y1 - y2)^2}}$
    public double getDistanceTo(Point2D other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    @Override
    public String toString() {
        return String.format("%d -> %.3f", ID, getDistanceTo(this));
    }
}

class DistanceItem {

    private Point2D point;
    private double distance;
    private int index;

    public DistanceItem(Point2D point, double distance) {
        this.point = point;
        this.distance = distance;
        this.index = index;
    }

    public Point2D getPoint() {
        return point;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return String.format("%d -> %.3f", point.getID(), distance);
    }
}

class Cluster<T extends Point2D> {

    private List<T> items;

    public Cluster() {
        this.items = new ArrayList<>();
    }

    public void addItem(T element) {
        items.add(element);
    }

    public void near(long ID, int top) {
        Point2D target = findTargetByID(ID);

        if (target == null) {
            System.out.printf("%s not found ID!\n", ID);
            return;
        }

        List<DistanceItem> distanceItemList = items.stream()
                .filter(item -> item.getID() != ID)
                .map(item -> new DistanceItem(item, calculateDistance(target, item)))
                .sorted(Comparator.comparingDouble(DistanceItem::getDistance))
                .limit(top)
                .collect(Collectors.toList());

        /*
        List<DistanceItem> distanceItemList = new ArrayList<>();
        for (T item : items) {
            if (item.getID() != ID) {
                double distance = target.getDistanceTo(item);
                distanceItemList.add(new DistanceItem(item, distance));
            }
        }
        distanceItemList.sort(Comparator.comparingDouble(DistanceItem::getDistance));
        */

        for (int i = 0; i < distanceItemList.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, distanceItemList.get(i));
        }
    }

    private Point2D findTargetByID(long ID) {
        for (T item : items) {
            if (item.getID() == ID) {
                return item;
            }
        }

        return null;
    }

    private double calculateDistance(Point2D target, T item) {
        return target.getDistanceTo(item);
    }
}

public class ClusterTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}