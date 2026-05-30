package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Book;
import model.BorrowRecord;
import model.Reader;

public class FileHandler {
    private static final String DATA_FOLDER = "data";
    private static final String BOOK_FILE = DATA_FOLDER + "/books.txt";
    private static final String READER_FILE = DATA_FOLDER + "/readers.txt";
    private static final String BORROW_RECORD_FILE = DATA_FOLDER + "/borrow_records.txt";

    private static void createDataFolderIfNeeded() {
        File dataFolder = new File(DATA_FOLDER);

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    public static void saveBooks(List<Book> books) throws IOException {
        createDataFolderIfNeeded();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOK_FILE))) {
            for (Book book : books) {
                writer.write(book.getBookId() + ";"
                        + book.getTitle() + ";"
                        + book.getAuthor() + ";"
                        + book.getCategory() + ";"
                        + book.getQuantity() + ";"
                        + book.getAvailableQuantity());
                writer.newLine();
            }
        }
    }

    public static List<Book> loadBooks() throws IOException {
        List<Book> books = new ArrayList<>();
        File bookFile = new File(BOOK_FILE);

        if (!bookFile.exists()) {
            return books;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(bookFile))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";", -1);

                if (parts.length != 6) {
                    System.err.println("Bo qua dong sach sai dinh dang: " + line);
                    continue;
                }

                try {
                    String bookId = parts[0];
                    String title = parts[1];
                    String author = parts[2];
                    String category = parts[3];
                    int quantity = Integer.parseInt(parts[4]);
                    int availableQuantity = Integer.parseInt(parts[5]);

                    Book book = new Book(bookId, title, author, category, quantity, availableQuantity);
                    books.add(book);
                } catch (IllegalArgumentException e) {
                    System.err.println("Bo qua dong sach khong hop le: " + line);
                }
            }
        }

        return books;
    }

    public static void saveReaders(List<Reader> readers) throws IOException {
        createDataFolderIfNeeded();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(READER_FILE))) {
            for (Reader reader : readers) {
                writer.write(reader.getId() + ";"
                        + reader.getName() + ";"
                        + reader.getPhone() + ";"
                        + reader.getEmail() + ";"
                        + reader.getAddress());
                writer.newLine();
            }
        }
    }

    public static List<Reader> loadReaders() throws IOException {
        List<Reader> readers = new ArrayList<>();
        File readerFile = new File(READER_FILE);

        if (!readerFile.exists()) {
            return readers;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(readerFile))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";", -1);

                if (parts.length != 5) {
                    System.err.println("Bo qua dong nguoi muon sai dinh dang: " + line);
                    continue;
                }

                String id = parts[0];
                String name = parts[1];
                String phone = parts[2];
                String email = parts[3];
                String address = parts[4];

                Reader loadedReader = new Reader(id, name, phone, email, address);
                readers.add(loadedReader);
            }
        }

        return readers;
    }

    public static void saveBorrowRecords(List<BorrowRecord> records) throws IOException {
        createDataFolderIfNeeded();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BORROW_RECORD_FILE))) {
            for (BorrowRecord record : records) {
                writer.write(record.getRecordId() + ";"
                        + record.getBookId() + ";"
                        + record.getReaderId() + ";"
                        + record.getBorrowDate() + ";"
                        + record.getReturnDate() + ";"
                        + record.isReturned());
                writer.newLine();
            }
        }
    }

    public static List<BorrowRecord> loadBorrowRecords() throws IOException {
        List<BorrowRecord> records = new ArrayList<>();
        File borrowRecordFile = new File(BORROW_RECORD_FILE);

        if (!borrowRecordFile.exists()) {
            return records;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(borrowRecordFile))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";", -1);

                if (parts.length != 6) {
                    System.err.println("Bo qua dong phieu muon sai dinh dang: " + line);
                    continue;
                }

                String recordId = parts[0];
                String bookId = parts[1];
                String readerId = parts[2];
                String borrowDate = parts[3];
                String returnDate = parts[4];
                String returnedText = parts[5].trim();

                if (!returnedText.equalsIgnoreCase("true") && !returnedText.equalsIgnoreCase("false")) {
                    System.err.println("Bo qua dong phieu muon co trang thai returned khong hop le: " + line);
                    continue;
                }

                boolean returned = Boolean.parseBoolean(returnedText);

                BorrowRecord record = new BorrowRecord(
                        recordId,
                        bookId,
                        readerId,
                        borrowDate,
                        returnDate,
                        returned
                );

                records.add(record);
            }
        }

        return records;
    }
}
