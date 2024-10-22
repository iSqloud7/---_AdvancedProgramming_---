package SecondColloquium.ColloquiumAssignments._2BookCollection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

class Book {

    private String bookTitle;
    private String bookCategory;
    private float bookPrice;

    public Book(String bookTitle, String bookCategory, float bookPrice) {
        this.bookTitle = bookTitle;
        this.bookCategory = bookCategory;
        this.bookPrice = bookPrice;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public float getBookPrice() {
        return bookPrice;
    }

    @Override
    public String toString() {
        // Format: A (A) 29.41
        return String.format("%s (%s) %.2f",
                getBookTitle(), getBookCategory(), getBookPrice());
    }
}

class BookCollection {

    private List<Book> books;

    private final Comparator<Book> TitleAndPriceComparator =
            Comparator.comparing(Book::getBookTitle)
                    .thenComparing(Book::getBookPrice);
                    // .reversed()

    private final Comparator<Book> PriceComparator =
            Comparator.comparing(Book::getBookPrice)
                    .thenComparing(Book::getBookTitle);
                    // .reversed()

    /*
    // AnonymousClass
    private final Comparator<Book> TitleAndPriceComparator = new Comparator<Book>() {
        @Override
        public int compare(Book book1, Book book2) {
            int result = book1.getBookTitle().compareTo(book2.getBookTitle());

            if (result == 0) {
                return Float.compare(book1.getBookPrice(), book2.getBookPrice());
            } else {
                return result;
            }
        }
    };
    // LambdaExpression
    private final Comparator<Book> TitleAndPriceComparator = (book1, book2) -> {
        int result = book1.getBookTitle().compareTo(book2.getBookTitle());

        if (result == 0) {
            return Float.compare(book1.getBookPrice(), book2.getBookPrice());
        } else {
            return result;
        }
    };
    // AnonymousClass
    private final Comparator<Book> PriceComparator = new Comparator<Book>() {
        @Override
        public int compare(Book book1, Book book2) {
            int result = Float.compare(book1.getBookPrice(), book2.getBookPrice());

            if (result == 0) {
                return book1.getBookTitle().compareTo(book2.getBookTitle());
            } else {
                return result;
            }
        }
    };
    // LambdaExpression
    private final Comparator<Book> PriceComparator = (book1, book2) -> {
        int result = Float.compare(book1.getBookPrice(), book2.getBookPrice());

        if (result == 0) {
            return book1.getBookCategory().compareTo(book2.getBookCategory());
        } else {
            return result;
        }
    };
    */

    public BookCollection() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void printByCategory(String category) {
        books.stream()
                .filter(book -> book.getBookCategory().equals(category))
                .sorted(TitleAndPriceComparator)
                .forEach(book -> System.out.println(book));
    }

    public List<Book> getCheapestN(int n) {
        return books.stream()
                .sorted(PriceComparator)
                .limit(n)
                .collect(Collectors.toList());
    }
}

public class BookCollectionTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}