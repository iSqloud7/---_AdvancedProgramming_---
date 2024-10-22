package SecondColloquium.ColloquiumAssignments._35OnlinePayments;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Item {

    private String itemName;
    private double itemPrice;

    public Item(String itemName, double itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }
}

class OnlinePayments {

    private Map<String, List<Item>> studentPayments;
    private static final double COMMISSION_PERCENTAGE = 1.14;
    private static final double MIN_COMMISSION = 3.0;
    private static final double MAX_COMMISSION = 300.0;

    public OnlinePayments() {
        this.studentPayments = new HashMap<>();
    }

    // STUDENT_IDX ITEM_NAME PRICE
    // 151020;Административно-материјални трошоци и осигурување;750
    public void readItems(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");

            if (parts.length != 3) {
                continue;
            }

            String studentIndex = parts[0];
            String itemName = parts[1];
            double itemPrice;
            try {
                itemPrice = Double.parseDouble(parts[2]);
            } catch (NumberFormatException e) {
                continue;
            }

            Item item = new Item(itemName, itemPrice);
            studentPayments.computeIfAbsent(studentIndex, k -> new ArrayList<>()).add(item);
        }

    }

    public void printStudentReport(String index, OutputStream os) {
        PrintWriter writer = new PrintWriter(os);

        List<Item> items = studentPayments.get(index);

        if (items == null) {
            // Student 151021 not found!
            writer.printf("Student %s not found!\n", index);
            writer.flush();
            return;
        }

        double netoAmount = items.stream()
                .mapToDouble(Item::getItemPrice)
                .sum();

        double commissionPercentage = netoAmount * COMMISSION_PERCENTAGE / 100;

        double bankCommission;
        if (commissionPercentage < MIN_COMMISSION) {
            bankCommission = MIN_COMMISSION;
        } else if (commissionPercentage > MAX_COMMISSION) {
            bankCommission = MAX_COMMISSION;
        } else {
            bankCommission = commissionPercentage;
        }

        double totalAmount = netoAmount + bankCommission;

        // Student: 151020 Net: 13050 Fee: 149 Total: 13199
        writer.printf("Student: %s Net: %.0f Fee: %.0f Total: %.0f\n",
                index, round(netoAmount), round(bankCommission), round(totalAmount));

        // Items:
        // 1. Школарина за летен семестар 2022/2023 12300
        // 2. Административно-материјални трошоци и осигурување 750
        items.sort(Comparator.comparing(Item::getItemPrice).reversed());

        writer.println("Items:");
        int ordNumber = 1;
        for (Item item : items) {
            writer.printf("%d. %s %.0f\n",
                    ordNumber++, item.getItemName(), round(item.getItemPrice()));
        }

        writer.flush();
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

public class OnlinePaymentsTest {

    public static void main(String[] args) throws IOException {

        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}