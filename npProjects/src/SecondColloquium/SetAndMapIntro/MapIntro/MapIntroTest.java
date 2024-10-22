package SecondColloquium.SetAndMapIntro.MapIntro;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapIntroTest {

    // Map е колекција на парови клуч и вредност [KEY, VALUE]
    // Генеричка колекција со два генерички параметри
    // Map -> TreeMap, HashMap & SetMap
    // Табела со две колони и бескрај редици
    // УПОТРЕБЛИВОСТ:
    // --- често за броење појавување на елементи
    // --- групирање, зборови кои содржат нешто -> Map<String, List<CollectionOfStrings>> (CBHT)

    public static void main(String[] args) {


        // TreeMap
        // УПОТРЕБЛИВОСТ:
        // --- клучот мора да биде Comparable (првиот објект)
        // --- мапата TreeMap е сортирана според клуч -> растечки редослед
        // --- во конструкторот на TreeMap-ата може да се додаде Comparator на типот на клучот
        // --- избегнува дупликати клучеви
        // додавање O(logn)
        // contains O(logn)
        // итерација O(nlogn)
        System.out.println("\n[][][]=== TreeMap ===[][][ ]");

        Map<String, String> treeMap = new TreeMap<>();

        treeMap.put("f", "finki");
        treeMap.put("I", "IVAN");
        treeMap.put("P", "PUPINOSKI");
        treeMap.put("NP", "NaprednoProgramiranje");
        treeMap.put("f", "FINKI"); // бришење на првиот Key -> f

        System.out.println(treeMap);


        // УПОТРЕБЛИВОСТ:
        // --- го изместува редоследот
        // --- елементите во тип клуч мора да го преоптоварат HashCode
        // --- додавање O(n)
        // --- contains O(1)
        // --- итерација O(n)
        // --- изместува редослед, мора override HashCodeMethod
        // --- наједноставна комплексност
        // --- комплексност O(1) како HashMap
        System.out.println("\n[][][]=== HashMap ===[][][]");

        Map<String, String> hashMap = new HashMap<>();

        hashMap.put("f", "finki");
        hashMap.put("I", "IVAN");
        hashMap.put("P", "PUPINOSKI");
        hashMap.put("NP", "NaprednoProgramiranje");
        hashMap.put("f", "FINKI"); // бришење на првиот Key -> f

        System.out.println(hashMap);


        // LinkedHashMap
        // УПОТРЕБЛИВОСТ:
        // --- чување редослед на додавање елементи
        System.out.println("\n[][][]=== LinkedHashMap ===[][][]");

        Map<String, String> linkedHashMap = new LinkedHashMap<>();

        linkedHashMap.put("f", "finki");
        linkedHashMap.put("I", "IVAN");
        linkedHashMap.put("P", "PUPINOSKI");
        linkedHashMap.put("NP", "NaprednoProgramiranje");
        linkedHashMap.put("f", "FINKI"); // бришење на првиот Key -> f

        System.out.println(linkedHashMap);
    }
}
