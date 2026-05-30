package app;

import model.Book;
import model.Reader;
import service.LibraryManager;

public class TestLibraryManager {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testBookAndReaderManagement();
        testBorrowAndReturnRules();
        testStatistics();

        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
    }

    private static void testBookAndReaderManagement() {
        LibraryManager manager = new LibraryManager();

        check("Add book success", manager.addBook(new Book("B001", "Java Core", "Author A", "CNTT", 2)));
        check("Reject duplicated book id", !manager.addBook(new Book("B001", "Java OOP", "Author B", "CNTT", 1)));
        check("Update existing book", manager.updateBook(new Book("B001", "Java Core Updated", "Author A", "CNTT", 3)));
        check("Search book by title", manager.searchBooks("updated").size() == 1);
        check("Remove existing book", manager.removeBook("B001"));

        check("Add reader success", manager.addReader(new Reader("R001", "Nguyen Van A", "0901000000", "a@gmail.com", "Ha Noi")));
        check("Reject duplicated reader id", !manager.addReader(new Reader("R001", "Tran Van B", "0902000000", "", "")));
        check("Update existing reader", manager.updateReader(new Reader("R001", "Nguyen Van A Updated", "0901999999", "", "")));
        check("Search reader by phone", manager.searchReaders("9999").size() == 1);
        check("Remove existing reader", manager.removeReader("R001"));
    }

    private static void testBorrowAndReturnRules() {
        LibraryManager manager = createManagerWithOneBookAndOneReader();

        check("Borrow book success", manager.borrowBook("BR001", "R001", "B001", "2026-05-29"));
        check("Book available quantity decreases", manager.findBookById("B001").getAvailableQuantity() == 0);
        check("Reject borrow when book is out of stock", !manager.borrowBook("BR002", "R001", "B001", "2026-05-29"));
        check("Reject removing borrowed book", !manager.removeBook("B001"));
        check("Reject removing reader with open record", !manager.removeReader("R001"));
        check("Return book success", manager.returnBook("BR001", "2026-05-30"));
        check("Book available quantity increases", manager.findBookById("B001").getAvailableQuantity() == 1);
        check("Reject returning same record twice", !manager.returnBook("BR001", "2026-05-31"));
        check("Allow removing returned book", manager.removeBook("B001"));
        check("Allow removing reader after return", manager.removeReader("R001"));
    }

    private static void testStatistics() {
        LibraryManager manager = new LibraryManager();
        manager.addBook(new Book("B001", "Java Core", "Author A", "CNTT", 2));
        manager.addBook(new Book("B002", "OOP Basic", "Author B", "CNTT", 3));
        manager.addReader(new Reader("R001", "Nguyen Van A", "0901000000", "", ""));

        manager.borrowBook("BR001", "R001", "B001", "2026-05-29");

        check("Count book titles", manager.countBookTitles() == 2);
        check("Count total book quantity", manager.countTotalBookQuantity() == 5);
        check("Count available book quantity", manager.countAvailableBookQuantity() == 4);
        check("Count borrowing records", manager.countBorrowingRecords() == 1);

        manager.returnBook("BR001", "2026-05-30");

        check("Available quantity after return", manager.countAvailableBookQuantity() == 5);
        check("Borrowing records after return", manager.countBorrowingRecords() == 0);
    }

    private static LibraryManager createManagerWithOneBookAndOneReader() {
        LibraryManager manager = new LibraryManager();
        manager.addBook(new Book("B001", "Java Core", "Author A", "CNTT", 1));
        manager.addReader(new Reader("R001", "Nguyen Van A", "0901000000", "", ""));
        return manager;
    }

    private static void check(String testName, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS - " + testName);
        } else {
            failed++;
            System.out.println("FAIL - " + testName);
        }
    }
}
