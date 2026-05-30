package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.BorrowRecord;
import storage.FileHandler;

public class TestBorrowRecordFileIO {
    public static void main(String[] args) {
        List<BorrowRecord> records = new ArrayList<>();

        records.add(new BorrowRecord("BR01", "B01", "R01", "2026-05-29"));
        records.add(new BorrowRecord("BR02", "B02", "R02", "2026-05-20", "2026-05-25", true));

        try {
            FileHandler.saveBorrowRecords(records);

            List<BorrowRecord> loadedRecords = FileHandler.loadBorrowRecords();

            for (BorrowRecord record : loadedRecords) {
                System.out.println(record);
            }
        } catch (IOException e) {
            System.out.println("Loi doc/ghi file: " + e.getMessage());
        }
    }
}