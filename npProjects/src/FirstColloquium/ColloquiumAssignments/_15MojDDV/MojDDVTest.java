package FirstColloquium.ColloquiumAssignments._15MojDDV;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

enum TaxType {

    A,
    B,
    V
}

class AmountNotAllowedException extends Exception {

    public AmountNotAllowedException(int amount) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", amount));
    }
}

class Item {

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

    public double getCalculatedTax() {
        if (taxType.equals(TaxType.A)) {
            return itemPrice * 0.18;
        } else if (taxType.equals(TaxType.B)) {
            return itemPrice * 0.05;
        } else {
            return 0.0;
        }
    }

    public double getDDV() {
        return getCalculatedTax() * 0.15;
    }
}

class Receipt {

    private String receiptID;
    private List<Item> items;

    public Receipt(String receiptID, List<Item> items) {
        this.receiptID = receiptID;
        this.items = items;
    }

    public String getReceiptID() {
        return receiptID;
    }

    public static Receipt createReceipt(String line) throws AmountNotAllowedException {
        //  0      1             2
        // ID item_price1 item_tax_type1 item_price2 item_tax_type2 … item_price-n item_tax_type-n
        String[] parts = line.split("\\s+");

        String receiptID = parts[0];

        List<Item> items = new ArrayList<>();
        Arrays.stream(parts)
                .skip(1)
                .forEach(i -> {
                    if (Character.isDigit(i.charAt(0))) {
                        items.add(new Item(Double.parseDouble(i)));
                    } else {
                        items.get(items.size() - 1).setTaxType(TaxType.valueOf(i));
                    }
                });

        if (totalAmount(items) > 30000) {
            throw new AmountNotAllowedException((int) totalAmount(items));
        }

        return new Receipt(receiptID, items);
    }

    private static double totalAmount(List<Item> items) {
        return items.stream()
                .mapToDouble(Item::getItemPrice)
                .sum();
    }

    private int getSumOfAmounts() {
        return items.stream()
                .mapToInt(i -> (int) i.getItemPrice())
                .sum();
    }

    private double getTaxReturns() {
        return items.stream()
                .mapToDouble(Item::getDDV)
                .sum();
    }

    @Override
    public String toString() {
        //    ID item_price1 item_tax_type1
        //  107228   27153       190.09

        return String.format("%s %d %.2f",
                getReceiptID(), getSumOfAmounts(), getTaxReturns());
    }
}

class MojDDV {

    private List<Receipt> receipts;

    public MojDDV() {
        this.receipts = new ArrayList<>();
    }

    /*
         ID item_price1 item_tax_type1 item_price2 item_tax_type2 … item_price-n item_tax_type-n
       12334  1789            А           1238          B              1222 V        111 V
    */
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
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);
    }
}