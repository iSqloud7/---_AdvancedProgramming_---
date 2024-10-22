package FirstColloquium.AudVezhbi04.Bank;

public class NonInterestCheckingAccount extends Account{

    public NonInterestCheckingAccount(String accountOwner, int ID, double currentAmount) {
        super(accountOwner, ID, currentAmount);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.NON_INTEREST;
    }

    public static void main(String[] args) {

    }
}
