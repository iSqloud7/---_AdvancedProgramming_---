package FirstColloquium.ColloquiumAssignments.MobileOperator;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class InvalidIdException extends Exception {

    public InvalidIdException(String message) {
        super(message);
    }
}

class IDValidator {

    public static boolean isValid(String ID, int lenght) {
        if (ID.length() != lenght) {
            return false;
        }

        for (int i = 0; i < lenght; i++) {
            if (!Character.isDigit(ID.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}

class S_Customer extends Customer {

    private static double BASE_PRICE_S_PACKET = 500.0;

    private static double FREE_MINUTES_S_PACKET = 100.0;
    private static double FREE_SMS_S_PACKET = 50.0;
    private static double FREE_GB_INTERNET_S_PACKET = 5.0;

    private static double PRICE_PER_MINUTES = 5.0;
    private static double PRICE_PER_SMS = 6.0;
    private static double PRICE_PER_GB_INTERNET = 25.0;

    private static double COMMISSION_RATE = 0.07;

    public S_Customer(String customerID, double minutes, int SMSs, double GBs) throws InvalidIdException {
        super(customerID, minutes, SMSs, GBs);
    }

    @Override
    public double totalPrice() {
        double total = BASE_PRICE_S_PACKET;

        /*
        if (minutes > FREE_MINUTES_S_PACKET) {
            total += (PRICE_PER_MINUTES * (minutes - FREE_MINUTES_S_PACKET));
        }

        if (SMSs > FREE_SMS_S_PACKET) {
            total += (PRICE_PER_SMS * (SMSs - FREE_SMS_S_PACKET));
        }

        if (GBs > FREE_GB_INTERNET_S_PACKET) {
            total += (PRICE_PER_GB_INTERNET * (GBs - FREE_GB_INTERNET_S_PACKET));
        }
        */

        total += PRICE_PER_MINUTES * Math.max(0, minutes - FREE_MINUTES_S_PACKET);
        total += PRICE_PER_SMS * Math.max(0, SMSs - FREE_SMS_S_PACKET);
        total += PRICE_PER_GB_INTERNET * Math.max(0, GBs - FREE_GB_INTERNET_S_PACKET);

        return total;
    }

    @Override
    public double commission() {
        return totalPrice() * COMMISSION_RATE;
    }
}

class M_Customer extends Customer {

    private static double BASE_PRICE_M_PACKET = 750.0;

    private static double FREE_MINUTES_M_PACKET = 150.0;
    private static double FREE_SMS_M_PACKET = 60.0;
    private static double FREE_GB_INTERNET_M_PACKET = 10.0;

    private static double PRICE_PER_MINUTES = 4.0;
    private static double PRICE_PER_SMS = 4.0;
    private static double PRICE_PER_GB_INTERNET = 20.0;

    private static double COMMISSION_RATE = 0.04;

    public M_Customer(String customerID, double minutes, int SMSs, double GBs) throws InvalidIdException {
        super(customerID, minutes, SMSs, GBs);
    }

    @Override
    public double totalPrice() {
        double total = BASE_PRICE_M_PACKET;

        total += PRICE_PER_MINUTES * Math.max(0, minutes - FREE_MINUTES_M_PACKET);
        total += PRICE_PER_SMS * Math.max(0, SMSs - FREE_SMS_M_PACKET);
        total += PRICE_PER_GB_INTERNET * Math.max(0, GBs - FREE_GB_INTERNET_M_PACKET);

        return total;
    }

    @Override
    public double commission() {
        return totalPrice() * COMMISSION_RATE;
    }
}

abstract class Customer { // Package

    private String customerID;
    protected double minutes;
    protected int SMSs;
    protected double GBs;

    /* replaced by class IDValidator
    private static boolean isIDValid(String ID) {
        if (ID.length() != 7) {
            return false;
        }

        for (int i = 0; i < 7; i++) {
            if (!Character.isDigit(ID.charAt(i))) {
                return false;
            }
        }

        return true;
    }
    */

    public Customer(String customerID, double minutes, int SMSs, double GBs) throws InvalidIdException {
        if (!IDValidator.isValid(customerID, 7)) { // !isIDValid(customerID)
            throw new InvalidIdException(String.format("%s is not a valid user ID\n", customerID));
        }

        this.customerID = customerID;
        this.minutes = minutes;
        this.SMSs = SMSs;
        this.GBs = GBs;
    }

    public abstract double totalPrice();

    public abstract double commission();
}

class SalesRep implements Comparable<SalesRep> {

    private String salesRepID;
    private List<Customer> customers;

    public SalesRep(String salesRepID, List<Customer> customers) {
        this.salesRepID = salesRepID;
        this.customers = customers;
    }

    /* replaced by class IDValidator
    private static boolean isIDValid(String ID) {
        if (ID.length() != 3) {
            return false;
        }

        for (int i = 0; i < 3; i++) {
            if (!Character.isDigit(ID.charAt(i))) {
                return false;
            }
        }

        return true;
    }
    */

    public static SalesRep createSalesRep(String line) throws InvalidIdException {
        /*
           (sales rep)
           salesRepID [customerBill1] [customerBill2] … [customeBillrN]
             0: 475 (customer bill)
                    customerID package_type count_of_minutes count_of_SMS count_of_data_in_GB
                    1: 4642771     2: M         3: 248           4: 90        5: 14.94
        */
        String[] parts = line.split("\\s+");

        String salesRepID = parts[0];

        if (!IDValidator.isValid(salesRepID, 3)) { // !isIDValid(salesRepID)
            throw new InvalidIdException((String.format("%s is not a valid sales rep ID\n", salesRepID)));
        }

        List<Customer> customers = new ArrayList<>();

        for (int i = 1; i < parts.length; i += 5) {
            String customerID = parts[i];
            String packageType = parts[i + 1];
            double minutes = Double.parseDouble(parts[i + 2]);
            int SMSs = Integer.parseInt(parts[i + 3]);
            double GBs = Double.parseDouble(parts[i + 4]);

            try {
                if (packageType.equals("S")) {
                    customers.add(new S_Customer(customerID, minutes, SMSs, GBs));
                } else {
                    customers.add(new M_Customer(customerID, minutes, SMSs, GBs));
                }
            } catch (InvalidIdException e) {
                System.out.println(e.getMessage());
            }
        }

        return new SalesRep(salesRepID, customers);
    }

    private double totalCommission() {
        return customers.stream()
                .mapToDouble(customer -> customer.commission())
                .sum();
    }

    @Override
    public String toString() {
        DoubleSummaryStatistics summaryStatistics = customers.stream()
                .mapToDouble(customer -> customer.totalPrice()).summaryStatistics();
        // ID number_of_bills min_bill average_bill max_bill total_commission
        // 475 Count: 4  Min: 724.75 Average: 1011.89 Max: 1360.80 Commission: 183.64

        return String.format("%s Count: %d Min: %.2f Average: %.2f Max: %.2f Commission: %.2f",
                salesRepID,
                summaryStatistics.getCount(),
                summaryStatistics.getMin(),
                summaryStatistics.getAverage(),
                summaryStatistics.getMax(),
                totalCommission());
    }

    @Override
    public int compareTo(SalesRep other) {
        return Double.compare(this.totalCommission(), other.totalCommission());
    }
}

class MobileOperator {

    private List<SalesRep> salesReps;

    public MobileOperator() {
        this.salesReps = new ArrayList<>();
    }

    /* INPUT: 475 4642771 M 248 90 14.94 2281930 S 139 48 6.19 4040003 M 189 100 11.90 5064198 M 159 78 9.32
       (sales rep)
       salesRepID [customerBill1] [customerBill2] … [customeBillrN]
          [475] (customer bill)
                customerID package_type count_of_minutes count_of_SMS count_of_data_in_GB
                [4642771]       [M]          [248]           [90]           [14.94]
                [2281930]       [S]          [139]           [48]           [6.19]
                [4040003]       [M]          [189]           [100]          [11.90]
                [5064198]       [M]          [159]           [78]           [9.32]
    */
    public void readSalesRepData(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        salesReps = reader.lines()
                .map(line -> {
                    try {
                        return SalesRep.createSalesRep(line);
                    } catch (InvalidIdException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        reader.close();
    }

    /*
       ID number_of_bills min_bill average_bill max_bill total_commission.
    */
    public void printSalesReport(OutputStream os) {
        PrintWriter writer = new PrintWriter(os);

        salesReps.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(salesRep -> writer.println(salesRep.toString()));

        writer.flush();
    }
}

public class MobileOperatorTest {

    public static void main(String[] args) {

        MobileOperator mobileOperator = new MobileOperator();
        System.out.println("---- READING OF THE SALES REPORTS ----");
        try {
            mobileOperator.readSalesRepData(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("---- PRINTING FINAL REPORTS FOR SALES REPRESENTATIVES ----");
        mobileOperator.printSalesReport(System.out);
    }
}
