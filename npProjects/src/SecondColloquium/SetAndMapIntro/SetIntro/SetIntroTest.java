package SecondColloquium.SetAndMapIntro.SetIntro;

import java.util.*;

public class SetIntroTest {

    // Set е множество во кое нема дупликати
    // Collection -> Set -> TreeSet, HashSet & LinkedHashSet
    // УПОТРЕБЛИВОСТ:
    // --- да нема дупликати

    public static void main(String[] args) {



        // TreeSet
        // УПОТРЕБЛИВОСТ:
        // --- да нема дупликати, само уникатни елементи
        // --- сортирање на елементите според некој Comparator
        // --- мора елементите да бидат Comparable, според имплементацијата ги подредува
        // --- ако се користи Comparator, мора да се прати како аргумент во конструкторот на TreeSet-от
        // пристап O(logn)
        // итерација O(nlogn)
        // додавање O(logn)
        // бришење O(nlogn)
        System.out.println("\n[][][]=== TreeSet ===[][][]");

        Set<Integer> treeIntSet1 = new TreeSet<>(); // default Comparator.naturalOrder()
        Set<Integer> treeIntSet2 = new TreeSet<>(Comparator.reverseOrder());

        for (int i = 1; i <= 10; i++) {
            treeIntSet1.add(i); // растечки редослед
            treeIntSet1.add(i); // не успева да ги печати, мора уникатни елементи да има во Set-от
            treeIntSet2.add(i); // опаѓачки редослед
        }

        System.out.println("NaturalOrder: " + treeIntSet1);
        System.out.println("ReversedOrder: " + treeIntSet2);



        // HashSet
        // УПОТРЕБЛИВОСТ:
        // --- нема дупликати, се избегнуваат според пресметаната вредност
        // --- не се предава ништо во конструкторот, освен самата колекцијата
        // --- не е според редослед -> LinkedHashSet(чување редослед на додавање)
        // --- функционира со хеш вредности
        // --- во класата во која се ставаат елементите со HashSet мора да се преоптовари HashCode
        // --- наједноставна комплексност
        // --- додавање n елементи со O(n) комплексност, инаку O(1)
        // --- итерирање O(n)
        System.out.println("\n[][][]=== HashSet ===[][][]");

        Set<Integer> hashIntSet = new HashSet<>();

        for (int i = 1; i <= 10; i++) {
            hashIntSet.add(i);
            hashIntSet.add(i);
        }

        System.out.println(hashIntSet);

        Set<String> hashStringSet = new HashSet<>();
        hashStringSet.add("FINKI");
        hashStringSet.add("IVAN");
        hashStringSet.add("PUPINOSKI");
        hashStringSet.add("NP");

        System.out.println(hashStringSet);



        // LinkedHashSet
        // УПОТРЕБЛИВОСТ:
        // --- чување редослед на додавање елементи
        // --- комплексност O(1) како HashSet
        System.out.println("\n[][][]=== LinkedHashSet ===[][][]");

        Set<Integer> linkedIntHashSet = new LinkedHashSet<>();

        for (int i = 1; i <= 10; i++) {
            linkedIntHashSet.add(i);
            linkedIntHashSet.add(i);
        }

        Set<String> linkedStringHashSet = new LinkedHashSet<>();

        linkedStringHashSet.add("FINKI");
        linkedStringHashSet.add("IVAN");
        linkedStringHashSet.add("PUPINOSKI");
        linkedStringHashSet.add("NP");
        linkedStringHashSet.add("FINKI"); // го наоѓа и ќе го пребрише првиот според кофичката

        System.out.println(linkedIntHashSet + " " + linkedStringHashSet);
    }
}
