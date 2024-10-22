package FirstColloquium.AudVezhbi04.Bank;

import java.util.Arrays;

public class Bank {

    private Account[] accounts;
    private int totalAccounts; // колку членови има внатре во низата
    private int maxAccounts; // големина на низа

    public Bank(int maxAccounts) {
        this.maxAccounts = maxAccounts;
        this.accounts = new Account[maxAccounts];
        this.totalAccounts = 0;
    }

    public void addAccount(Account account) {
        if (totalAccounts == maxAccounts) {
            accounts = Arrays.copyOf(accounts, maxAccounts * 2);
            maxAccounts *= 2;
        }

        accounts[totalAccounts++] = account;
    }

    public double totalAssets() {
        double sumOfAllAccounts = 0.0;

        for (Account account : accounts) {
            if (account != null) {
                sumOfAllAccounts += account.getCurrentAmount();
            }
        }

        return sumOfAllAccounts;
    }

    public void addInterestToAllAccounts() {

        /* instanceof
        for (Account account : accounts) {
            if (account instanceof InterestBearingAccount) {
                InterestBearingAccount interestBearingAccount = (InterestBearingAccount) account;
                interestBearingAccount.addInterest();
            }
        }
        */

        for (Account account : accounts) {
            if (account != null && account.getAccountType().equals(AccountType.INTEREST)) { // проверка дали е сметката е со камата
                InterestBearingAccount interestBearingAccount = (InterestBearingAccount) account; // мора кастирање кон поспецифичен тип, заедничко за сите класи е интерфејсот
                interestBearingAccount.addInterest();
            }
        }
    }

    public static void main(String[] args) {

    }
}
