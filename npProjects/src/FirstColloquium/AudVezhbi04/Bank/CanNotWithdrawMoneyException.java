package FirstColloquium.AudVezhbi04.Bank;

public class CanNotWithdrawMoneyException extends Exception {

    public CanNotWithdrawMoneyException(double currentAmount, double amount) {
        super(String.format("Your current amount is: %.2f, and you can not withdraw this amount: %.2f",
                currentAmount, amount));
    }
}
