package SecondColloquium.ColloquiumAssignments._21PhoneBook;

import java.util.*;

class DuplicateNumberException extends Exception {
    // Duplicate number: [number]

    public DuplicateNumberException(String phoneNumber) {
        super(String.format("Duplicate number: %s", phoneNumber));
    }
}

class Contact implements Comparable<Contact> {

    private String contactName;
    private String phoneNumber;

    public Contact(String contactName, String phoneNumber) {
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public int compareTo(Contact other) {
        int result = this.getContactName().compareTo(other.getContactName());

        if (result == 0) {
            return this.getPhoneNumber().compareTo(other.getPhoneNumber());
        } else {
            return result;
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s", getContactName(), getPhoneNumber());
    }
}

class PhoneBook {

    // to prevent duplicates
    private Set<String> allPhoneNumbers;
    // to group contacts by subnumber
    private Map<String, Set<Contact>> contactsBySubstring;
    // to group contacts by name
    private Map<String, Set<Contact>> contactsByName;

    public PhoneBook() {
        this.allPhoneNumbers = new HashSet<>();
        this.contactsBySubstring = new HashMap<>();
        this.contactsByName = new HashMap<>();
    }

    private List<String> getSubstring(String phoneNumber) {
        List<String> result = new ArrayList<>();
        for (int len = 3; len <= phoneNumber.length(); len++) {
            for (int i = 0; i <= phoneNumber.length() - len; i++) {
                result.add(phoneNumber.substring(i, i + len));
            }
        }

        return result;
    }

    public void addContact(String name, String phoneNumber) throws DuplicateNumberException {
        if (allPhoneNumbers.contains(phoneNumber)) {
            throw new DuplicateNumberException(phoneNumber);
        } else {
            allPhoneNumbers.add(phoneNumber);
            Contact contact = new Contact(name, phoneNumber);
            List<String> subNumbers = getSubstring(phoneNumber);

            for (String subNumber : subNumbers) {
                contactsBySubstring.putIfAbsent(subNumber, new TreeSet<>()); // ако не постои, празна листа
                contactsBySubstring.get(subNumber).add(contact);
            }

            contactsByName.putIfAbsent(name, new TreeSet<>());
            contactsByName.get(name).add(contact);
        }
    }

    public void contactsByNumber(String phoneNumber) {
        Set<Contact> contacts = contactsBySubstring.get(phoneNumber);

        if (contacts == null) {
            System.out.println("NOT FOUND");
            return;
        }

        contacts.forEach(contact -> System.out.println(contact.toString()));
    }

    public void contactsByName(String name) {
        Set<Contact> contacts = contactsByName.get(name);

        if (contacts == null) {
            System.out.println("NOT FOUND");
            return;
        }

        contacts.forEach(contact -> System.out.println(contact.toString()));
    }
}

public class PhoneBookTest {

    public static void main(String[] args) {

        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}