package FirstColloquium.AudVezhbi04.Bank;

public class InterestCheckingAccount extends Account implements InterestBearingAccount {

    public static final double INTEREST_RATE = 0.03;

    public InterestCheckingAccount(String accountOwner, int ID, double currentAmount) {
        super(accountOwner, ID, currentAmount);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.INTEREST;
    }

    @Override
    public void addInterest() {
        addAmount(getCurrentAmount() * INTEREST_RATE);
    }

    public static void main(String[] args) {

    }
}
