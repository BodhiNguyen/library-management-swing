package app;

import java.io.IOException;
import model.Book;
import service.LibraryManager;

public class TestBookFileIO {
    public static void main(String[] args) {
        LibraryManager manager = new LibraryManager();

        manager.addBook(new Book("B01", "Java Core", "Author A", "Programming", 5));
        manager.addBook(new Book("B02", "OOP Basic", "Author B", "Education", 3));

        try {
            manager.saveBooksToFile();

            LibraryManager loadedManager = new LibraryManager();
            loadedManager.loadBooksFromFile();

            for (Book book : loadedManager.getBooks()) {
                System.out.println(book);
            }
        } catch (IOException e) {
            System.out.println("Loi doc/ghi file: " + e.getMessage());
        }
    }
}