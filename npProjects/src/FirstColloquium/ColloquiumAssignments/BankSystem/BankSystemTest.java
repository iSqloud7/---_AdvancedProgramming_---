package FirstColloquium.ColloquiumAssignments.BankSystem;

import java.util.*;
import java.util.stream.Collectors;

// usage: AmountConverter.convertFromStringToDouble
class AmountConverter {

    public static double convertFromStringToDouble(String amount) {
        return Double.parseDouble(amount.substring(0, amount.length() - 1));
    }

    public static String convertFromDoubleToString(double amount) {
        return String.format("%.2f$", amount);
    }
}

class FlatAmountProvisionTransaction extends Transaction {

    private String flatProvision;

    public FlatAmountProvisionTransaction(long IDSender, long IDReceiver, String amount, String flatProvision) {
        super(IDSender, IDReceiver, "FlatAmount", amount);
        this.flatProvision = flatProvision;
    }

    public double getFlatProvision() {
        return AmountConverter.convertFromStringToDouble(flatProvision);
    }

    @Override
    public double getProvision() {
        return AmountConverter.convertFromStringToDouble(flatProvision);
    }
}

class FlatPercentProvisionTransaction extends Transaction {

    private int centsPerDollar;

    public FlatPercentProvisionTransaction(long IDSender, long IDReceiver, String amount, int centsPerDollar) {
        super(IDSender, IDReceiver, "FlatPercent", amount);
        this.centsPerDollar = centsPerDollar;
    }

    public int getCentsPerDollar() { // 15$ -> 1.5 && 15.75 -> 1.5
        return centsPerDollar;
    }

    @Override
    public double getProvision() {
        return (int) AmountConverter.convertFromStringToDouble(getAmount()) * ((float) centsPerDollar / 100.0);
    }
}

abstract class Transaction {

    private long IDSender;
    private long IDReceiver;
    private String description;
    private String amount;

    public Transaction(long IDSender, long IDReceiver, String description, String amount) {
        this.IDSender = IDSender;
        this.IDReceiver = IDReceiver;
        this.description = description;
        this.amount = amount;
    }

    public long getIDSender() {
        return IDSender;
    }

    public long getIDReceiver() {
        return IDReceiver;
    }

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
    }

    abstract public double getProvision();

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Transaction that = (Transaction) object;
        return IDSender == that.IDSender && IDReceiver == that.IDReceiver && Objects.equals(description, that.description) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(IDSender, IDReceiver, description, amount);
    }
}

class Account {

    private String userName;
    private long userID;
    private String userBalance;
    private static Random RANDOM = new Random();

    public Account(String userName, String userBalance) {
        this.userName = userName;
        this.userBalance = userBalance;
        this.userID = RANDOM.nextLong();
    }

    public String getUserName() {
        return userName;
    }

    public long getUserID() {
        return userID;
    }

    public String getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(String userBalance) {
        this.userBalance = userBalance;
    }

    @Override
    public String toString() {
        // Name:Andrej Gajduk\n Balance:20.00$\n

        return String.format("Name: %s\nBalance: %s\n",
                getUserName(), getUserBalance());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Account account = (Account) object;
        return userID == account.userID && Objects.equals(userName, account.userName) && Objects.equals(userBalance, account.userBalance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, userID, userBalance);
    }

    public boolean hasEnoughMoney(double totalAmount) {
        return totalAmount <=
                AmountConverter.convertFromStringToDouble(getUserBalance());
    }
}

class Bank {

    private String bankName;
    private Account[] accounts;
    private double transfers;
    private double provisions;

    public Bank(String bankName, Account[] accounts) {
        this.bankName = bankName;
        // сопствена копија на низата од сметки
        this.accounts = new Account[accounts.length];

        System.arraycopy(accounts, 0, this.accounts, 0, accounts.length);
        // or
        /*
            for (int i = 0; i < accounts.length; i++) {
            this.accounts[i] = accounts[i];
            }
        */
    }

    public String getBankName() {
        return bankName;
    }

    public Account[] getAccounts() {
        return accounts;
    }

    private Account findAccount(long ID) {
        /*
           for (int i = 0; i < accounts.length; i++) {
            if (accounts[i].getUserID() == ID) {
                return accounts[i];
            }
           }
        */

        for (Account account : accounts) {
            if (account.getUserID() == ID) {
                return account;
            }
        }

        return null;
    }

    // 1. Дали корисникот има доволно средства на сметка
    // 2. Дали двете сметки при трансакцијата се во банката
    public boolean makeTransaction(Transaction transaction) {
        Account from = findAccount(transaction.getIDSender());
        Account to = findAccount(transaction.getIDReceiver());

        if (from == null || to == null) { // 2. Дали двете сметки при трансакцијата се во банката
            return false;
        }

        double transactionAmount = AmountConverter.convertFromStringToDouble(transaction.getAmount());
        double totalAmount = transactionAmount + transaction.getProvision();

        if (!from.hasEnoughMoney(totalAmount)) { // 1. Дали корисникот има доволно средства на сметка
            return false;
        }

        double newBalanceForSender = AmountConverter.convertFromStringToDouble(from.getUserBalance()) - totalAmount;
        double newBalanceForReceiver = AmountConverter.convertFromStringToDouble(from.getUserBalance()) + transactionAmount;

        from.setUserBalance(AmountConverter.convertFromDoubleToString(newBalanceForSender));
        to.setUserBalance(AmountConverter.convertFromDoubleToString(newBalanceForReceiver));

        transfers += transactionAmount;
        provisions += transaction.getProvision();

        return true;
    }

    public String totalProvision() {
        return AmountConverter.convertFromDoubleToString(provisions);
    }

    public String totalTransfers() {
        return AmountConverter.convertFromDoubleToString(transfers);
    }

    @Override
    public String toString() {
        // Name:Banka na RM\n -> податоци за сите корисници

        StringBuilder builder = new StringBuilder();

        builder.append(String.format("\nName: %s", getBankName()))
                .append("\n\n");

        for (Account account : accounts) {
            builder.append(account.toString());
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Bank bank = (Bank) object;
        return Double.compare(transfers, bank.transfers) == 0 && Double.compare(provisions, bank.provisions) == 0 && Objects.equals(bankName, bank.bankName) && Arrays.equals(accounts, bank.accounts);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(bankName, transfers, provisions);
        result = 31 * result + Arrays.hashCode(accounts);
        return result;
    }
}

public class BankSystemTest {

    public static void main(String[] args) {

        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1) && !a1.equals(a2) && !a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getUserID).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Tes", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        //accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getUserID();
        long to_id = a3.getUserID();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getUserID(),
                        bank.getAccounts()[to_idx].getUserID(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getUserID(),
                        bank.getAccounts()[to_idx].getUserID(), amount, Integer.parseInt(o));
        }
        return null;
    }
}