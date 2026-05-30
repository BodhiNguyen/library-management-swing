package app;

import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import service.LibraryManager;
import ui.MainFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LibraryManager manager = new LibraryManager();

                try {
                    manager.loadData();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Loi doc du lieu tu file: " + e.getMessage());
                }

                MainFrame frame = new MainFrame(manager);
                frame.setVisible(true);
            }
        });
    }
}
