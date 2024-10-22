package FirstColloquium.AudVezhbi04.Bank;

public abstract class Account {

    private String accountOwner;
    private int ID; // ID на моментална сметка
    private double currentAmount;
    private static int IDSeed = 1000;  // на ниво на класа, IDSeed++
    private AccountType accountType;

    public Account(String accountOwner, int ID, double currentAmount) {
        this.accountOwner = accountOwner;
        this.currentAmount = currentAmount;
        // this.accountType = accountType; -> варијанта преку конструктор
//        this.ID = IDSeed++;
//        or
        this.ID = IDSeed;
        IDSeed++;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void addAmount(double amount) {
        this.currentAmount += amount;
    }

    public void withdrawAmount(double amount) throws CanNotWithdrawMoneyException {
        if (currentAmount < amount) {
            throw new CanNotWithdrawMoneyException(currentAmount, amount);
        }

        this.currentAmount -= amount;
    }

    public abstract AccountType getAccountType();

    @Override
    public String toString() {
        return String.format("%d: %.2f", ID, currentAmount);
    }

    public static void main(String[] args) {

    }
}
