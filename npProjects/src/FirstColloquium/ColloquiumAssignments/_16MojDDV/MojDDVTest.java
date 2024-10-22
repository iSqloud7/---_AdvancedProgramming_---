package FirstColloquium.ColloquiumAssignments._16MojDDV;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

enum TaxType {

    A,
    B,
    V
}

class Item {
    // item_price1 item_tax_type1
    //     1789         А

    private double itemPrice;
    private TaxType taxType;

    public Item(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Item(double itemPrice, TaxType taxType) {
        this.itemPrice = itemPrice;
        this.taxType = taxType;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = taxType;
    }

    /*
       А (18% од вредноста)
       B (5% од вредноста)
       V (0% од вредноста)
    */
    public double getCalculatedTax() {
        if (taxType.equals(TaxType.A)) {
            return getItemPrice() * 0.18;
        } else if (taxType.equals(TaxType.B)) {
            return getItemPrice() * 0.05;
        } else {
            return 0.0;
        }
    }

    // Повратокот на ДДВ изнесува 15% од данокот на додадената вредност за артикалот.
    public double getDDV() {
        return getCalculatedTax() * 0.15;
    }
}

class AmountNotAllowedException extends Exception {

    public AmountNotAllowedException(int amount) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", amount));
    }
}

class Receipt {
    //  ID   item_price1 item_tax_type1 item_price2 item_tax_type2 … item_price-n item_tax_type-n
    // 2334    1789           А            1238          B               1222          V

    private long receiptID;
    private List<Item> items;

    public Receipt(long receiptID, List<Item> items) {
        this.receiptID = receiptID;
        this.items = items;
    }

    public long getReceiptID() {
        return receiptID;
    }

    public static Receipt createReceipt(String line) throws AmountNotAllowedException {
        //  ID   item_price1 item_tax_type1 item_price2 item_tax_type2 … item_price-n item_tax_type-n
        // 2334    1789           А            1238          B               1222          V
        String[] parts = line.split("\\s+");

        long receiptID = Long.parseLong(parts[0]);

        List<Item> items = new ArrayList<>();
        Arrays.stream(parts)
                .skip(1)
                .forEach(item -> {
                    if (Character.isDigit(item.charAt(0))) {
                        items.add(new Item(Double.parseDouble(item)));
                    } else {
                        items.get(items.size() - 1).setTaxType(TaxType.valueOf(item));
                    }
                });

        if (totalAmount(items) > 30000) {
            // Receipt with amount [сума на сите артикли] is not allowed to be scanned
            // Receipt with amount 34832 is not allowed to be scanned
            throw new AmountNotAllowedException((int) totalAmount(items));
        }

        return new Receipt(receiptID, items);
    }

    private static double totalAmount(List<Item> items) {
        return items.stream()
                .mapToDouble(Item::getItemPrice)
                .sum();
    }

    private int getTotalAmount() {
        return items.stream()
                .mapToInt(item -> (int) item.getItemPrice())
                .sum();
    }

    public double getTaxReturns() {
        return items.stream()
                .mapToDouble(Item::getDDV)
                .sum();
    }

    @Override
    public String toString() {
        // Печатењето на вредностите во методот printTaxReturns се врши на тој начин што:
        // сите информации се одвоени со таб
        // -id-то i сумата на фискалната сметка се печатат со 10 места
        // -повратокот на ДДВ со 10 места, од кои 5 се за цифрите после децималата.
        // ID SUM_OF_AMOUNTS TAX_RETURN

        return String.format("%10d\t%10d\t%10.5f",
                getReceiptID(), getTotalAmount(), getTaxReturns());
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
                        return Receipt.createReceipt(line);
                    } catch (AmountNotAllowedException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .collect(Collectors.toList());

        receipts = receipts.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void printTaxReturns(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);

        receipts.stream()
                .forEach(receipt -> writer.println(receipt.toString()));

        writer.flush();
    }

    /*
     min: MIN max: MAX sum: SUM count: COUNT average: AVERAGE,
     при што секоја од статистиките е во нов ред,
     а пак вредноста на статистиката е оддалечена со таб од името на статистиката.
     Децималните вредности се печатат со 5 места, од кои 3 се за цифрите после децималата.
     Целите вредности се пишуваат со 5 места (порамнети на лево).
    */
    public void printStatistics(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);
        DoubleSummaryStatistics statistics = receipts.stream()
                .mapToDouble(Receipt::getTaxReturns)
                .summaryStatistics();

        writer.println(String.format("min:\t%.3f\nmax:\t%.3f\nsum:\t%.3f\ncount:\t%d\navg:\t%.3f\n",
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

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}