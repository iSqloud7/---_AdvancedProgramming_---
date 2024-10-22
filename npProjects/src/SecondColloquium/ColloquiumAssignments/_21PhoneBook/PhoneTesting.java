package SecondColloquium.ColloquiumAssignments._21PhoneBook;

import java.util.ArrayList;
import java.util.List;

public class PhoneTesting {

    private static List<String> getSubstring(String phoneNumber) {
        List<String> result = new ArrayList<>();
        for (int length = 3; length <= phoneNumber.length(); length++) {
            for (int i = 0; i <= phoneNumber.length() - length; i++) {
                result.add(phoneNumber.substring(i, i + length));
            }
        }

        return result;
    }

    public static void main(String[] args) {

        String phoneNumber = "071123456";

        System.out.println(getSubstring(phoneNumber));
    }
}

