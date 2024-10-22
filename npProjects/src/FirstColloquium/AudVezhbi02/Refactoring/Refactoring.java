package FirstColloquium.AudVezhbi02.Refactoring;

public class Refactoring {

    /*
    public int countAllNumbersDivisibleWithDigitsSum(int start, int end) {
        int count = 0;

        for (int i = start; i <= end; i++) {
            int sumOfDigits = 0;
            int tmp = i;

            // Select Method -> Refactor -> Extract Method
            while (tmp > 0) {
                sumOfDigits += (tmp % 10);
                tmp /= 10;
            }

            if (i % sumOfDigits == 0) {
                count++;
            }
        }

        return count;
    }
    */

    public int countAllNumbersDivisibleWithDigitsSum(int start, int end) {
        int count = 0;

        for (int i = start; i <= end; i++) {
            if (i % getSumOfDigits(i) == 0) {
                count++;
            }
        }

        return count;
    }

    private static int getSumOfDigits(int number) {
        int sum = 0;
        while (number > 0) {
            sum += (number % 10);
            number /= 10;
        }

        return sum;
    }
}
