package FirstColloquium.AudVezhbi02.StringPrefix;

public class StringPrefix {

    public static boolean isPrefix(String firstString, String secondString) {
        if (firstString.length() > secondString.length()) {
            return false;
        }

        for (int i = 0; i < firstString.length(); i++) {
            if (firstString.charAt(i) != secondString.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        System.out.println("Is prefix? " + isPrefix("napredno_programiranje", "napredno_programiranjeNP"));
        System.out.println("Is prefix? " + isPrefix("ivan", "pupinoski"));
        System.out.println("Is prefix? " + isPrefix("finki", "finki"));
    }
}
