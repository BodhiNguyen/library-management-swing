package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Book;
import model.BorrowRecord;
import model.Reader;
import storage.FileHandler;

public class LibraryManager {
    private List<Book> books;
    private List<Reader> readers;
    private List<BorrowRecord> borrowRecords;

    public LibraryManager() {
        books = new ArrayList<>();
        readers = new ArrayList<>();
        borrowRecords = new ArrayList<>();
    }

    public boolean addBook(Book book) {
        if (book == null) {
            return false;
        }

        if (findBookById(book.getBookId()) != null) {
            return false;
        }

        books.add(book);
        return true;
    }

    public boolean removeBook(String bookId) {
        Book book = findBookById(bookId);
        if (book == null) {
            return false;
        }

        for (BorrowRecord record : borrowRecords) {
            if (record.getBookId().equalsIgnoreCase(bookId) && !record.isReturned()) {
                return false;
            }
        }

        books.remove(book);
        return true;
    }

    public boolean updateBook(Book updatedBook) {
        if (updatedBook == null) {
            return false;
        }

        Book book = findBookById(updatedBook.getBookId());
        if (book == null) {
            return false;
        }

        try {
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setCategory(updatedBook.getCategory());
            book.setQuantity(updatedBook.getQuantity());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean addReader(Reader reader) {
        if (reader == null) {
            return false;
        }
        
        if (findReaderById(reader.getId()) != null) {
            return false;
        }

        readers.add(reader);
        return true;
    }

    public boolean updateReader(Reader updatedReader) {
        if (updatedReader == null) {
            return false;
        }

        Reader reader = findReaderById(updatedReader.getId());
        if (reader == null) {
            return false;
        }

        reader.setName(updatedReader.getName());
        reader.setPhone(updatedReader.getPhone());
        reader.setEmail(updatedReader.getEmail());
        reader.setAddress(updatedReader.getAddress());

        return true;
    }

    public boolean removeReader(String readerId) {
        Reader reader = findReaderById(readerId);
        if (reader == null) {
            return false;
        }

        for (BorrowRecord record : borrowRecords) {
            if (record.getReaderId().equalsIgnoreCase(readerId) && !record.isReturned()) {
                return false;
            }
        }

        readers.remove(reader);
        return true;
    }

    public Book findBookById(String bookId) {
        if (bookId == null) {
            return null;
        }

        for (Book book : books) {
            if (book.getBookId().equalsIgnoreCase(bookId)) {
                return book;
            }
        }

        return null;
    }

    public Reader findReaderById(String readerId) {
        if (readerId == null) {
            return null;
        }

        for (Reader reader : readers) {
            if (reader.getId().equalsIgnoreCase(readerId)) {
                return reader;
            }
        }

        return null;
    }

    public BorrowRecord findBorrowRecordById(String recordId) {
        if (recordId == null) {
            return null;
        }

        for (BorrowRecord record : borrowRecords) {
            if (record.getRecordId().equalsIgnoreCase(recordId)) {
                return record;
            }
        }

        return null;
    }

    public boolean borrowBook(String recordId, String readerId, String bookId, String borrowDate) {
        Reader reader = findReaderById(readerId);
        if (reader == null) {
            return false;
        }

        Book book = findBookById(bookId);
        if (book == null) {
            return false;
        }

        if (findBorrowRecordById(recordId) != null) {
            return false;
        }

        boolean borrowed = book.borrowOne();
        if (!borrowed) {
            return false;
        }

        BorrowRecord record = new BorrowRecord(recordId, bookId, readerId, borrowDate);
        borrowRecords.add(record);

        return true;
    }

    public boolean returnBook(String recordId, String returnDate) {
        BorrowRecord record = findBorrowRecordById(recordId);
        if (record == null) {
            return false;
        }

        if (record.isReturned()) {
            return false;
        }

        Book book = findBookById(record.getBookId());
        if (book == null) {
            return false;
        }

        boolean returnedBook = book.returnOne();
        if (!returnedBook) {
            return false;
        }

        boolean returnedRecord = record.markReturned(returnDate);
        if (!returnedRecord) {
            return false;
        }

        return true;
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> result = new ArrayList<>();

        for (Book book : books) {
            if (book.matchesKeyword(keyword)) {
                result.add(book);
            }
        }

        return result;
    }

    public List<Reader> searchReaders(String keyword) {
        List<Reader> result = new ArrayList<>();

        for (Reader reader : readers) {
            if (reader.matchesKeyword(keyword)) {
                result.add(reader);
            }
        }

        return result;
    }

    public int countBorrowingRecords() {
        int count = 0;

        for (BorrowRecord record : borrowRecords) {
            if (!record.isReturned()) {
                count++;
            }
        }

        return count;
    }

    public int countBookTitles() {
        return books.size();
    }

    public int countTotalBookQuantity() {
        int total = 0;

        for (Book book : books) {
            total += book.getQuantity();
        }

        return total;
    }

    public int countAvailableBookQuantity() {
        int total = 0;

        for (Book book : books) {
            total += book.getAvailableQuantity();
        }

        return total;
    }

    public void loadData() throws IOException {
        books = FileHandler.loadBooks();
        readers = FileHandler.loadReaders();
        borrowRecords = FileHandler.loadBorrowRecords();
    }

    public void saveData() throws IOException {
        FileHandler.saveBooks(books);
        FileHandler.saveReaders(readers);
        FileHandler.saveBorrowRecords(borrowRecords);
    }

    public void loadBooksFromFile() throws IOException {
        books = FileHandler.loadBooks();
    }

    public void saveBooksToFile() throws IOException {
        FileHandler.saveBooks(books);
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Reader> getReaders() {
        return readers;
    }

    public List<BorrowRecord> getBorrowRecords() {
        return borrowRecords;
    }
}
