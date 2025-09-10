import java.util.ArrayList;
import java.util.Scanner;

public class LibraryManagement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library library = new Library();
        int choice = -1;

        while (choice != 6) {
            System.out.println("\n===== Library Menu =====");
            System.out.println("1. Add Book");
            System.out.println("2. Show Books");
            System.out.println("3. Borrow Book (by ID)");
            System.out.println("4. Return Book (by ID)");
            System.out.println("5. Search Book (by title)");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                sc.nextLine();
                System.out.println("Please enter a number.");
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter title: ");
                    String title = sc.nextLine().trim();
                    System.out.print("Enter author: ");
                    String author = sc.nextLine().trim();
                    Book added = library.addBook(title, author);
                    System.out.println("Book added with ID: " + added.getId());
                }
                case 2 -> library.showBooks();
                case 3 -> {
                    System.out.print("Enter book ID to borrow: ");
                    if (!sc.hasNextInt()) { sc.nextLine(); System.out.println("Invalid ID."); break; }
                    int id = sc.nextInt(); sc.nextLine();
                    library.borrowBook(id);
                }
                case 4 -> {
                    System.out.print("Enter book ID to return: ");
                    if (!sc.hasNextInt()) { sc.nextLine(); System.out.println("Invalid ID."); break; }
                    int id = sc.nextInt(); sc.nextLine();
                    library.returnBook(id);
                }
                case 5 -> {
                    System.out.print("Enter title keyword to search: ");
                    String kw = sc.nextLine().trim();
                    library.searchByTitle(kw);
                }
                case 6 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice, try again.");
            }
        }

        sc.close();
    }
}

class Book {
    private final int id;
    private final String title;
    private final String author;
    private boolean isAvailable;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean v) { this.isAvailable = v; }
}

class Library {
    private final ArrayList<Book> books = new ArrayList<>();
    private int nextId = 1;

    public Book addBook(String title, String author) {
        Book b = new Book(nextId++, title, author);
        books.add(b);
        return b;
    }

    public void showBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }
        System.out.printf("%-4s %-30s %-20s %-10s%n", "ID", "Title", "Author", "Status");
        for (Book b : books) {
            System.out.printf("%-4d %-30s %-20s %-10s%n",
                    b.getId(),
                    truncate(b.getTitle(), 30),
                    truncate(b.getAuthor(), 20),
                    b.isAvailable() ? "Available" : "Borrowed");
        }
    }

    public void borrowBook(int id) {
        Book b = findById(id);
        if (b == null) {
            System.out.println("Book not found (ID: " + id + ").");
        } else if (!b.isAvailable()) {
            System.out.println("Book already borrowed: " + b.getTitle());
        } else {
            b.setAvailable(false);
            System.out.println("You borrowed: " + b.getTitle());
        }
    }

    public void returnBook(int id) {
        Book b = findById(id);
        if (b == null) {
            System.out.println("Book not found (ID: " + id + ").");
        } else if (b.isAvailable()) {
            System.out.println("Book is already marked available: " + b.getTitle());
        } else {
            b.setAvailable(true);
            System.out.println("You returned: " + b.getTitle());
        }
    }

    public void searchByTitle(String keyword) {
        boolean found = false;
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                if (!found) {
                    System.out.printf("%-4s %-30s %-20s %-10s%n", "ID", "Title", "Author", "Status");
                    found = true;
                }
                System.out.printf("%-4d %-30s %-20s %-10s%n",
                        b.getId(),
                        truncate(b.getTitle(), 30),
                        truncate(b.getAuthor(), 20),
                        b.isAvailable() ? "Available" : "Borrowed");
            }
        }
        if (!found) System.out.println("No books matched: " + keyword);
    }

    private Book findById(int id) {
        return books.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }

    private String truncate(String s, int len) {
        if (s == null) return "";
        return s.length() <= len ? s : s.substring(0, len - 3) + "...";
    }
}