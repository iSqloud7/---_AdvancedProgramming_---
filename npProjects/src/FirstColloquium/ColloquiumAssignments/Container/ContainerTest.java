package FirstColloquium.ColloquiumAssignments.Container;

import java.util.List;
import java.util.Scanner;

public class ContainerTest {

    public static void main(String[] args) {

        Container<WeightableDouble> container1 = new Container();
        Container<WeightableDouble> container2 = new Container();
        Container<WeightableString> container3 = new Container();

        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int p = scanner.nextInt();

        double a = scanner.nextDouble();
        double b = scanner.nextDouble();

        WeightableDouble wa = new WeightableDouble(a);
        WeightableDouble wb = new WeightableDouble(b);

        for (int i = 0; i < n; i++) {
            double weight = scanner.nextDouble();
            container1.addElement(new WeightableDouble(weight));
        }

        for (int i = 0; i < m; i++) {
            double weight = scanner.nextDouble();
            container2.addElement(new WeightableDouble(weight));
        }

        for (int i = 0; i < p; i++) {
            String s = scanner.next();
            container3.addElement(new WeightableString(s));
        }

        List<WeightableDouble> resultSmaller = container1.lighterThan(wa);
        List<WeightableDouble> resultBetween = container1.between(wa, wb);

        System.out.println("Lighter than " + wa.getWeight() + ":");

        for (WeightableDouble wd : resultSmaller) {
            System.out.println(wd.getWeight());
        }

        System.out.println("Between " + wa.getWeight() + " and " + wb.getWeight() + ":");

        for (WeightableDouble wd : resultBetween) {
            System.out.println(wd.getWeight());
        }

        System.out.println("Comparaison: ");
        System.out.println(container1.compare(container2));
        System.out.println(container1.compare(container3));
    }
}
