package ru.x5.school.advanced.collections.task;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class TestApp {
    private final Supplier<BookManagerBasic> managerSupplier;
    private Client client1;
    private Client client2;
    private Client client3;
    private Client client4;
    private Client client5;
    private Client client6;

    private BookDescription book1;
    private BookDescription book2;
    private BookDescription book3;
    private BookDescription book4;
    private BookDescription book5;
    private BookDescription book6;


    public TestApp(Supplier<BookManagerBasic> managerSupplier) {
        this.managerSupplier = managerSupplier;
        init();
    }

    private void init() {
        client1 = new Client("Петр", "Михайлов", "320804506");
        client2 = new Client("Васисуалий", "Лоханкин", "987344234");
        client3 = new Client("Нечай", "Петров", "3422110233");
        client4 = new Client("Алевтина", "Ангилоки", "342342344");
        client5 = new Client("Ингеборга", "Дапкунайте", "2343242342");
        client6 = new Client("Стивен", "Спилберг", "530804306");

        book1 = new BookDescription("The Hitchhiker's guide to the Galaxy", "Douglas Adams", 1979);
        book2 = new BookDescription("Core Java. Volume I - Fundamentals", "Horstmann C.S.", 2019);
        book3 = new BookDescription("Core Java. Volume II - Advanced Features", "Horstmann C.S.", 2019);
        book4 = new BookDescription("Былое и думы", "А. И. Герцен", 1868);
        book5 = new BookDescription("Cryptonomicon", "Neal Stephenson", 1999);
        book6 = new BookDescription("В глубине великого кристалла", "Владислав Крапивин", 1989);
    }

    public void runTests() {
        testSingleBookAddition();
        testBooksList();
        testBooksRentAll();
        testMultiBookAddition();
        testBooksList();
        testRentBook();
        testRentBookTwice();
        testRentAbsentBook();
        testRentAbsentBook2();
        testReturnBook();
        testReturnBook2();
        testRentBooks();
        testRentBooks2();
        testRentChampions();
        testRentBooksByCount();
        testRentedBooksCount();
        testRentedBooksCount2();
        testRentChampions2();
    }

    public void testSingleBookAddition() {
        var manager = managerSupplier.get();
        assert(manager.getBookAmount(book1) == 0);
        assert(manager.addBook(book1) == 1 );
        assert(manager.getBookAmount(book1) == 1);
        assert(manager.addBook(book1) == 2);
        assert(manager.getBookAmount(book1) == 2);
    }

    public void testMultiBookAddition() {
        var manager = managerSupplier.get();
        assert(manager.getBookAmount(book2) == 0);
        assert(manager.addBook(book2, 3) == 3);
        assert(manager.getBookAmount(book2) == 3);
        assert(manager.addBook(book2, 2) == 5);
        assert(manager.getBookAmount(book2) == 5);
    }

    public void testRentBook() {
        var manager = managerSupplier.get();
        manager.addBook(book2, 3);

        assert(manager.rentBook(client1, book2));
        assert(manager.getBookAmount(book2) == 2);
        var clientBooks = manager.getRentedBooks(client1);
        assert(clientBooks.size() == 1);
        assert(clientBooks.contains(book2));
    }

    public void testRentBookTwice() {
        var manager = managerSupplier.get();
        manager.addBook(book1, 3);

        assert(manager.rentBook(client1, book1));
        assert(!manager.rentBook(client1, book1));
        var clientBooks = manager.getRentedBooks(client1);
        assert(clientBooks.size() == 1);
        assert(clientBooks.contains(book1));
    }

    public void testRentAbsentBook() {
        var manager = managerSupplier.get();
        assert(!manager.rentBook(client1, book1));
    }

    public void testRentAbsentBook2() {
        var manager = managerSupplier.get();
        manager.addBook(book3);
        assert(manager.rentBook(client3, book3));
        assert(!manager.rentBook(client1, book3));
    }

    public void testReturnBook() {
        var manager = managerSupplier.get();
        assert(!manager.returnBook(client3, book2));
        assert(manager.getRentedBooks(client3).size() == 0);
    }

    public void testReturnBook2() {
        var manager = managerSupplier.get();
        manager.addBook(book3);
        manager.rentBook(client4, book3);
        assert(manager.returnBook(client4, book3));
        assert(manager.getBookAmount(book3) == 1);
        assert(manager.getRentedBooks(client4).size() == 0);
    }


    public void testRentBooks() {
        var manager = managerSupplier.get();
        fillBooks(manager);
        rentBooks(manager);

        var clientBooks = manager.getRentedBooks(client1);
        assert(clientBooks.size() == 4);
        assert(clientBooks.contains(book2));
        assert(clientBooks.contains(book3));
        assert(clientBooks.contains(book4));
        assert(clientBooks.contains(book5));

        clientBooks.add(book6);
        var clientBooks2 = manager.getRentedBooks(client1);
        assert(clientBooks2.size() == 4);
    }

    public void testRentBooks2() {
        var manager = managerSupplier.get();
        manager.addBook(book1, 2);

        manager.rentBook(client1, book1);
        manager.rentBook(client2, book1);

        var renters = manager.getBookRenters(book1);
        assert(renters.size() == 2);
        assert(renters.contains(client1));
        assert(renters.contains(client2));
        boolean exceptionHappened = false;
        try {
            renters.add(client3);
        } catch (UnsupportedOperationException e) {
            exceptionHappened = true;
        }
        assert (exceptionHappened);
    }


    private void fillBooks(BooksManager manager) {
        manager.addBook(book1);
        manager.addBook(book2, 3);
        manager.addBook(book3, 5);
        manager.addBook(book4, 7);
        manager.addBook(book5, 5);
        manager.addBook(book6, 2);
    }

    private void rentBooks(BooksManager manager) {
        manager.rentBook(client1, book2);
        manager.rentBook(client1, book3);
        manager.rentBook(client1, book5);
        manager.rentBook(client1, book4);

        manager.rentBook(client2, book3);
        manager.rentBook(client2, book6);
        manager.rentBook(client2, book1);

        manager.rentBook(client3, book5);

        manager.rentBook(client4, book2);
        manager.rentBook(client4, book5);

        manager.rentBook(client5, book2);
        manager.rentBook(client5, book3);
        manager.rentBook(client5, book4);
        manager.rentBook(client5, book5);
        manager.rentBook(client5, book6);

        manager.rentBook(client6, book4);
        manager.rentBook(client6, book3);
    }

    public void testBooksList() {
        var manager = managerSupplier.get();
        fillBooks(manager);
        rentBooks(manager);
        var books = manager.getRentedBooks(client1);
        assert(books.size() == 4);
        books.add(book6);
        assert(manager.getRentedBooks(client1).size() == 4);
    }

    public void testBooksRentAll() {
        var manager = managerSupplier.get();
        fillBooks(manager);
        rentBooks(manager);

        assert(manager.getBookAmount(book1) == 0);
        assert(manager.getBookAmount(book2) == 0);
        assert(manager.getBookAmount(book3) == 1);
        assert(manager.getBookAmount(book4) == 4);
        assert(manager.getBookAmount(book5) == 1);
        assert(manager.getBookAmount(book6) == 0);
    }

    public void testRentChampions() {
        var manager = managerSupplier.get();
        fillBooks(manager);
        rentBooks(manager);

        List<Client> rentChampions = manager.getRentChampions();
        assert(rentChampions.size() == 3);
        assert rentChampions.get(0).equals(client5);
        assert rentChampions.get(1).equals(client1);
        assert rentChampions.get(2).equals(client2);
    }

    public void testRentChampions2() {
        var manager = managerSupplier.get();
        fillBooks(manager);

        manager.rentBook(client2, book1);
        manager.rentBook(client2, book2);
        manager.rentBook(client1, book3);

        List<Client> rentChampions = manager.getRentChampions();
        assert(rentChampions.size() == 2);
        assert rentChampions.get(0).equals(client2);
        assert rentChampions.get(1).equals(client1);
    }

    public void testRentBooksByCount() {
        var manager = managerSupplier.get();
        fillBooks(manager);
        rentBooks(manager);

        List<BookDescription> booksByCount = manager.getBooksByCount();
        assert(booksByCount.size() == 6);
        assert(booksByCount.get(0).equals(book4));
    }

    public void testRentedBooksCount() {
        var manager = managerSupplier.get();
        fillBooks(manager);
        rentBooks(manager);

        var rentedBooks = manager.getRentedBooks();
        assert(rentedBooks.size() == 6);
        assert(rentedBooks.get(book1) == 1);
        assert(rentedBooks.get(book2) == 3);
        assert(rentedBooks.get(book3) == 4);
        assert(rentedBooks.get(book4) == 3);
        assert(rentedBooks.get(book5) == 4);
        assert(rentedBooks.get(book6) == 2);
    }

    public void testRentedBooksCount2() {
        var manager = managerSupplier.get();
        manager.addBook(book1);
        manager.addBook(book2);

        var rentedBooks = manager.getRentedBooks();
        assert(rentedBooks.size() == 2);
        assert(rentedBooks.get(book1) == 0);
        assert(rentedBooks.get(book2) == 0);
    }
    static Logger log = Logger.getLogger("mainTestApp");
    public static void main(String[] args) {
        TestApp testApp = new TestApp(BookManagerBasic::new);
        log.info("Start Test");
        testApp.runTests();
    }
}
