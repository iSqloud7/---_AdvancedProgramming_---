package FirstColloquium.AudVezhbi04.Bank;

public class PlatinumCheckingAccount extends InterestCheckingAccount {

    public PlatinumCheckingAccount(String accountOwner, int ID, double currentAmount) {
        super(accountOwner, ID, currentAmount);
    }

    @Override
    public void addInterest() {
        addAmount(getCurrentAmount() * INTEREST_RATE * 2);
    }

    public static void main(String[] args) {

    }
}
