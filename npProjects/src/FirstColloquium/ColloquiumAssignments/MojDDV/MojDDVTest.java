package FirstColloquium.ColloquiumAssignments.MojDDV;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class AmountNotAllowedException extends Exception {

    public AmountNotAllowedException(int amount) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", amount));
    }
}

enum TaxType {
    A,
    B,
    V
}

class Item {

    private int price;
    private TaxType taxType;

    public Item(int price, TaxType taxType) {
        this.price = price;
        this.taxType = taxType;
    }

    public Item(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = taxType;
    }

    public double getCalculatedTax() {
        if (taxType.equals(TaxType.A)) {
            return price * 0.18;
        } else if (taxType.equals(TaxType.B)) {
            return price * 0.05;
        } else {
            return 0;
        }
    }
}

class Receipt implements Comparable<Receipt> {

    private long ID;
    private List<Item> items;

    public Receipt(long ID) {
        this.ID = ID;
        this.items = new ArrayList<>();
    }

    public Receipt(long ID, List<Item> items) {
        this.ID = ID;
        this.items = items;
    }

    public static Receipt create(String line) throws AmountNotAllowedException {
        String[] parts = line.split("\\s+");
        long ID = Long.parseLong(parts[0]);
        List<Item> items = new ArrayList<>();

        Arrays.stream(parts)
                .skip(1)
                .forEach(i -> {
                    if (Character.isDigit(i.charAt(0))) {
                        items.add(new Item(Integer.parseInt(i)));
                    } else {
                        items.get(items.size() - 1).setTaxType(TaxType.valueOf(i));
                    }
                });

        if (totalAmount(items) > 30000) {
            throw new AmountNotAllowedException(totalAmount(items));
        } else {
            return new Receipt(ID, items);
        }
    }

    private static int totalAmount(List<Item> items) {
        return items.stream()
                .mapToInt(i -> i.getPrice())
                .sum();
    }

    private int totalAmount() {
        return items.stream()
                .mapToInt(i -> i.getPrice())
                .sum();
    }

    public double taxReturns() {
        return items.stream()
                .mapToDouble(i -> i.getCalculatedTax())
                .sum();
    }

    public long getID() {
        return ID;
    }

    @Override
    public int compareTo(Receipt other) {
        return Comparator.comparing(Receipt::taxReturns)
                .thenComparing(Receipt::totalAmount)
                .compare(this, other);
    }

    @Override
    public String toString() {
        return String.format("%10d\t%10d\t%5.5f", ID, totalAmount(), taxReturns());
    }
}

class MojDDV {

    private List<Receipt> receipts;

    public MojDDV() {
        this.receipts = new ArrayList<>();
    }

    public void readRecords(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        receipts = reader.lines()
                .map(line -> {
                    try {
                        return Receipt.create(line);
                    } catch (AmountNotAllowedException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .collect(Collectors.toList());

        receipts = receipts.stream()
                .filter(obj -> Objects.nonNull(obj))
                .collect(Collectors.toList());
    }

    public void printSorted(PrintStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);

        receipts.stream()
                .sorted()
                .forEach(i -> writer.println(i.toString()));

        writer.flush();
    }

    public void printStatistics(PrintStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);
        DoubleSummaryStatistics statistics = new DoubleSummaryStatistics();

        statistics = receipts.stream()
                .mapToDouble(Receipt::taxReturns).summaryStatistics();

        writer.println(String.format("min:\t%05.03f\nmax:%05.03f\nsum:%05.03f\ncount:%-5d\navg:%05.03f\n",
                statistics.getMin(),
                statistics.getMax(),
                statistics.getSum(),
                statistics.getCount(),
                statistics.getAverage()));

        writer.flush();
    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING SORTED RECORDS BY TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printSorted(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);
    }
}