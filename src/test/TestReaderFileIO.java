package app;

import java.io.IOException;
import java.util.List;
import model.Reader;
import service.LibraryManager;
import storage.FileHandler;

public class TestReaderFileIO {
    public static void main(String[] args) {
        LibraryManager manager = new LibraryManager();

        manager.addReader(new Reader("R01", "Nguyen Dang Khoa", "0906365802", "khoand081@gmail.com", "Gia Lai"));
        manager.addReader(new Reader("R02", "Nguyen Thien Ly", "0906365803", "khoand083@gmail.com", "Gia Lai"));

        try {
            FileHandler.saveReaders(manager.getReaders());

            List<Reader> loadedReaders = FileHandler.loadReaders();

            for (Reader reader : loadedReaders) {
                System.out.println(reader);
            }

        } catch (IOException e) {
            System.out.println("Loi doc/ghi file: " + e.getMessage());
        }
    }
}