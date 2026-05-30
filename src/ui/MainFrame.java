package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.Book;
import model.BorrowRecord;
import model.Reader;
import service.LibraryManager;

public class MainFrame extends JFrame {
    private final LibraryManager libraryManager;
    private JTabbedPane tabbedPane;

    private JTextField txtBookId;
    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JTextField txtCategory;
    private JTextField txtQuantity;
    private JTable bookTable;
    private DefaultTableModel bookTableModel;

    private JTextField txtReaderId;
    private JTextField txtReaderName;
    private JTextField txtReaderPhone;
    private JTextField txtReaderEmail;
    private JTextField txtReaderAddress;
    private JTable readerTable;
    private DefaultTableModel readerTableModel;

    private JTextField txtRecordId;
    private JComboBox<String> cmbBorrowReaderId;
    private JComboBox<String> cmbBorrowBookId;
    private JTextField txtBorrowDate;
    private JTextField txtReturnDate;
    private JTable borrowTable;
    private DefaultTableModel borrowTableModel;

    private JLabel lblTotalBookTitles;
    private JLabel lblTotalBookQuantity;
    private JLabel lblBorrowedBookQuantity;
    private JLabel lblBorrowingRecords;


    public MainFrame(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;

        setTitle("Quan ly muon tra sach thu vien");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Sach", createBookPanel());
        tabbedPane.addTab("Nguoi muon", createReaderPanel());
        tabbedPane.addTab("Muon/Tra", createBorrowPanel());
        tabbedPane.addTab("Thong ke", createStatisticPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createBookPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(createBookFormPanel(), BorderLayout.NORTH);
        panel.add(createBookTablePanel(), BorderLayout.CENTER);
        panel.add(createBookButtonPanel(), BorderLayout.SOUTH);
        
        reloadBookTable(libraryManager.getBooks());

        return panel;
    }

    private JPanel createBookFormPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        txtBookId = new JTextField();
        txtTitle = new JTextField();
        txtAuthor = new JTextField();
        txtCategory = new JTextField();
        txtQuantity = new JTextField();

        panel.add(new JLabel("Ma sach:"));
        panel.add(txtBookId);

        panel.add(new JLabel("Ten sach:"));
        panel.add(txtTitle);

        panel.add(new JLabel("Tac gia:"));
        panel.add(txtAuthor);

        panel.add(new JLabel("The loai:"));
        panel.add(txtCategory);

        panel.add(new JLabel("So luong:"));
        panel.add(txtQuantity);

        return panel;
    }

    private JScrollPane createBookTablePanel() {
        String[] columns = {"Ma sach", "Ten sach", "Tac gia", "The loai", "So luong", "Con lai"};

        bookTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(bookTableModel);

        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillBookFormFromSelectedRow();
            }
        });

        return new JScrollPane(bookTable);
    }

    private JPanel createBookButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnAdd = new JButton("Them");
        JButton btnUpdate = new JButton("Sua");
        JButton btnDelete = new JButton("Xoa");
        JButton btnSearch = new JButton("Tim kiem");
        JButton btnRefresh = new JButton("Lam moi");

        btnAdd.addActionListener(e -> addBook());
        btnUpdate.addActionListener(e -> updateBook());
        btnDelete.addActionListener(e -> deleteBook());
        btnSearch.addActionListener(e -> searchBooks());
        btnRefresh.addActionListener(e -> refreshBooks());

        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnSearch);
        panel.add(btnRefresh);

        return panel;
    }

    private void reloadBookTable(List<Book> books) {
        bookTableModel.setRowCount(0);

        for (Book book : books) {
            Object[] row = {
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getQuantity(),
                book.getAvailableQuantity()
            };

            bookTableModel.addRow(row);
        }
    }

    private Book getBookFromInput() {
        String bookId = txtBookId.getText().trim();
        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String category = txtCategory.getText().trim();
        String quantityText = txtQuantity.getText().trim();

        if (bookId.isEmpty() || title.isEmpty() || author.isEmpty()
            || category.isEmpty() || quantityText.isEmpty()) {
            throw new IllegalArgumentException("Vui long nhap day du thong tin sach.");
        }

        int quantity = Integer.parseInt(quantityText);

        return new Book(bookId, title, author, category, quantity);
    }

    private void addBook() {
        try {
            Book book = getBookFromInput();

            boolean success = libraryManager.addBook(book);

            if (!success) {
                JOptionPane.showMessageDialog(this, "Khong the them sach. Ma sach co the da ton tai.");
                return;
            }

            if (!saveDataSafely()) {
                return;
            }

            reloadBorrowComboBoxes();
            reloadBookTable(libraryManager.getBooks());
            reloadStatistics();
            clearBookForm();
            JOptionPane.showMessageDialog(this, "Them sach thanh cong.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "So luong phai la so nguyen.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void updateBook() {
        try {
            Book book = getBookFromInput();

            boolean success = libraryManager.updateBook(book);

            if (!success) {
                JOptionPane.showMessageDialog(this, "Khong the sua sach. Kiem tra ma sach hoac so luong.");
                return;
            }

            if (!saveDataSafely()) {
                return;
            }

            reloadBorrowComboBoxes();
            reloadBookTable(libraryManager.getBooks());
            reloadStatistics();
            clearBookForm();
            JOptionPane.showMessageDialog(this, "Sua sach thanh cong.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "So luong sach phai la so nguyen.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteBook() {
        String bookId = txtBookId.getText().trim();

        if (bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui long chon hoac nhap ma sach can xoa.");
            return;
        }

        boolean success = libraryManager.removeBook(bookId);

        if (!success) {
            JOptionPane.showMessageDialog(this, "Khong the xoa sach. Sach khong ton tai hoac dang duoc muon.");
            return;
        }

        if (!saveDataSafely()) {
            return;
        }

        reloadBorrowComboBoxes();
        reloadBookTable(libraryManager.getBooks());
        reloadStatistics();
        clearBookForm();
        JOptionPane.showMessageDialog(this, "Xoa sach thanh cong.");
    }

    private void searchBooks() {
        String keyword = JOptionPane.showInputDialog(this, "Nhap tu khoa tim kiem:");

        if (keyword == null) {
            return;
        }

        reloadBookTable(libraryManager.searchBooks(keyword.trim()));
    }

    private void refreshBooks() {
        reloadBookTable(libraryManager.getBooks());
        clearBookForm();
    }

    private void clearBookForm() {
        txtBookId.setText("");
        txtTitle.setText("");
        txtAuthor.setText("");
        txtCategory.setText("");
        txtQuantity.setText("");
        bookTable.clearSelection();
    }

    private void fillBookFormFromSelectedRow() {
        int selectedRow = bookTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        txtBookId.setText(bookTableModel.getValueAt(selectedRow, 0).toString());
        txtTitle.setText(bookTableModel.getValueAt(selectedRow, 1).toString());
        txtAuthor.setText(bookTableModel.getValueAt(selectedRow, 2).toString());
        txtCategory.setText(bookTableModel.getValueAt(selectedRow, 3).toString());
        txtQuantity.setText(bookTableModel.getValueAt(selectedRow, 4).toString());
    }

    private JPanel createReaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(createReaderFormPanel(), BorderLayout.NORTH);
        panel.add(createReaderTablePanel(), BorderLayout.CENTER);
        panel.add(createReaderButtonPanel(), BorderLayout.SOUTH);

        reloadReaderTable(libraryManager.getReaders());

        return panel;
    }

    private JPanel createReaderFormPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        txtReaderId = new JTextField();
        txtReaderName = new JTextField();
        txtReaderPhone = new JTextField();
        txtReaderEmail = new JTextField();
        txtReaderAddress = new JTextField();

        panel.add(new JLabel("Ma nguoi muon:"));
        panel.add(txtReaderId);

        panel.add(new JLabel("Ho ten:"));
        panel.add(txtReaderName);

        panel.add(new JLabel("So dien thoai:"));
        panel.add(txtReaderPhone);

        panel.add(new JLabel("Email:"));
        panel.add(txtReaderEmail);

        panel.add(new JLabel("Dia chi:"));
        panel.add(txtReaderAddress);

        return panel;
    }

    private JScrollPane createReaderTablePanel() {
        String[] columns = {"Ma nguoi muon", "Ho ten", "So dien thoai", "Email", "Dia chi"};

        readerTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        readerTable = new JTable(readerTableModel);

        readerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillReaderFormFromSelectedRow();
            }
        });

        return new JScrollPane(readerTable);
    }

    private JPanel createReaderButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnAdd = new JButton("Them");
        JButton btnUpdate = new JButton("Sua");
        JButton btnDelete = new JButton("Xoa");
        JButton btnSearch = new JButton("Tim kiem");
        JButton btnRefresh = new JButton("Lam moi");

        btnAdd.addActionListener(e -> addReader());
        btnUpdate.addActionListener(e -> updateReader());
        btnDelete.addActionListener(e -> deleteReader());
        btnSearch.addActionListener(e -> searchReaders());
        btnRefresh.addActionListener(e -> refreshReaders());

        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnSearch);
        panel.add(btnRefresh);

        return panel;
    }

    private void reloadReaderTable(List<Reader> readers) {
        readerTableModel.setRowCount(0);

        for (Reader reader : readers) {
            Object[] row = {
                reader.getId(),
                reader.getName(),
                reader.getPhone(),
                reader.getEmail(),
                reader.getAddress()
            };

            readerTableModel.addRow(row);
        }
    }

    private Reader getReaderFromInput() {
        String id = txtReaderId.getText().trim();
        String name = txtReaderName.getText().trim();
        String phone = txtReaderPhone.getText().trim();
        String email = txtReaderEmail.getText().trim();
        String address = txtReaderAddress.getText().trim();

        if (id.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            throw new IllegalArgumentException("Vui long nhap ma, ho ten va so dien thoai.");
        }

        return new Reader(id, name, phone, email, address);
    }

    private void addReader() {
        try {
            Reader reader = getReaderFromInput();

            boolean success = libraryManager.addReader(reader);

            if (!success) {
                JOptionPane.showMessageDialog(this, "Khong the them nguoi muon. Ma nguoi muon co the da ton tai.");
                return;
            }

            if (!saveDataSafely()) {
                return;
            }

            reloadBorrowComboBoxes();
            reloadReaderTable(libraryManager.getReaders());
            clearReaderForm();
            JOptionPane.showMessageDialog(this, "Them nguoi muon thanh cong.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void updateReader() {
        try {
            Reader reader = getReaderFromInput();

            boolean success = libraryManager.updateReader(reader);

            if (!success) {
                JOptionPane.showMessageDialog(this, "Khong the sua nguoi muon. Kiem tra ma nguoi muon.");
                return;
            }

            if (!saveDataSafely()) {
                return;
            }

            reloadBorrowComboBoxes();
            reloadReaderTable(libraryManager.getReaders());
            clearReaderForm();
            JOptionPane.showMessageDialog(this, "Sua nguoi muon thanh cong.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteReader() {
        String readerId = txtReaderId.getText().trim();

        if (readerId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui long chon hoac nhap ma nguoi muon can xoa.");
            return;
        }

        boolean success = libraryManager.removeReader(readerId);

        if (!success) {
            JOptionPane.showMessageDialog(this, "Khong the xoa nguoi muon. Nguoi muon khong ton tai hoac dang muon sach.");
            return;
        }

        if (!saveDataSafely()) {
            return;
        }

        reloadBorrowComboBoxes();
        reloadReaderTable(libraryManager.getReaders());
        clearReaderForm();
        JOptionPane.showMessageDialog(this, "Xoa nguoi muon thanh cong.");
    }

    private void searchReaders() {
        String keyword = JOptionPane.showInputDialog(this, "Nhap tu khoa tim kiem:");

        if (keyword == null) {
            return;
        }

        reloadReaderTable(libraryManager.searchReaders(keyword.trim()));
    }

    private void refreshReaders() {
        reloadReaderTable(libraryManager.getReaders());
        clearReaderForm();
    }

    private void clearReaderForm() {
        txtReaderId.setText("");
        txtReaderName.setText("");
        txtReaderPhone.setText("");
        txtReaderEmail.setText("");
        txtReaderAddress.setText("");
        readerTable.clearSelection();
    }

    private void fillReaderFormFromSelectedRow() {
        int selectedRow = readerTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        txtReaderId.setText(readerTableModel.getValueAt(selectedRow, 0).toString());
        txtReaderName.setText(readerTableModel.getValueAt(selectedRow, 1).toString());
        txtReaderPhone.setText(readerTableModel.getValueAt(selectedRow, 2).toString());
        txtReaderEmail.setText(readerTableModel.getValueAt(selectedRow, 3).toString());
        txtReaderAddress.setText(readerTableModel.getValueAt(selectedRow, 4).toString());
    }

    private JPanel createBorrowPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(createBorrowFormPanel(), BorderLayout.NORTH);
        panel.add(createBorrowTablePanel(), BorderLayout.CENTER);
        panel.add(createBorrowButtonPanel(), BorderLayout.SOUTH);

        refreshBorrowTab();

        return panel;
    }

    private JPanel createBorrowFormPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        txtRecordId = new JTextField();
        cmbBorrowReaderId = new JComboBox<>();
        cmbBorrowBookId = new JComboBox<>();
        txtBorrowDate = new JTextField();
        txtReturnDate = new JTextField();

        panel.add(new JLabel("Ma phieu muon:"));
        panel.add(txtRecordId);

        panel.add(new JLabel("Ma nguoi muon:"));
        panel.add(cmbBorrowReaderId);

        panel.add(new JLabel("Ma sach:"));
        panel.add(cmbBorrowBookId);

        panel.add(new JLabel("Ngay muon:"));
        panel.add(txtBorrowDate);

        panel.add(new JLabel("Ngay tra:"));
        panel.add(txtReturnDate);

        return panel;
    }

    private JScrollPane createBorrowTablePanel() {
        String[] columns = {"Ma phieu", "Ma nguoi muon", "Ma sach", "Ngay muon", "Ngay tra", "Trang thai"};

        borrowTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        borrowTable = new JTable(borrowTableModel);

        borrowTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillBorrowFormFromSelectedRow();
            }
        });

        return new JScrollPane(borrowTable);
    }

    private JPanel createBorrowButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnBorrow = new JButton("Muon sach");
        JButton btnReturn = new JButton("Tra sach");
        JButton btnRefresh = new JButton("Lam moi");

        btnBorrow.addActionListener(e -> borrowBook());
        btnReturn.addActionListener(e -> returnBook());
        btnRefresh.addActionListener(e -> refreshBorrowTab());

        panel.add(btnBorrow);
        panel.add(btnReturn);
        panel.add(btnRefresh);

        return panel;
    }

    private void refreshBorrowTab() {
        reloadBorrowReaderComboBox();
        reloadBorrowBookComboBox();
        reloadBorrowTable();
        clearBorrowForm();
    }

    private void reloadBorrowComboBoxes() {
        reloadBorrowReaderComboBox();
        reloadBorrowBookComboBox();
    }

    private void reloadBorrowReaderComboBox() {
        cmbBorrowReaderId.removeAllItems();

        for (Reader reader : libraryManager.getReaders()) {
            cmbBorrowReaderId.addItem(reader.getId());
        }
    }

    private void reloadBorrowBookComboBox() {
        cmbBorrowBookId.removeAllItems();

        for (Book book : libraryManager.getBooks()) {
            cmbBorrowBookId.addItem(book.getBookId());
        }
    }

    private void reloadBorrowTable() {
        borrowTableModel.setRowCount(0);

        for (BorrowRecord record : libraryManager.getBorrowRecords()) {
            Object[] row = {
                record.getRecordId(),
                record.getReaderId(),
                record.getBookId(),
                record.getBorrowDate(),
                record.getReturnDate(),
                record.isReturned() ? "Da tra" : "Dang muon"
            };

            borrowTableModel.addRow(row);
        }
    }

    private void borrowBook() {
        String recordId = txtRecordId.getText().trim();
        String borrowDate = txtBorrowDate.getText().trim();

        Object readerItem = cmbBorrowReaderId.getSelectedItem();
        Object bookItem = cmbBorrowBookId.getSelectedItem();

        if (recordId.isEmpty() || borrowDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui long nhap ma phieu va ngay muon.");
            return;
        }

        if (readerItem == null || bookItem == null) {
            JOptionPane.showMessageDialog(this, "Can co nguoi muon va sach truoc khi muon.");
            return;
        }

        String readerId = readerItem.toString();
        String bookId = bookItem.toString();

        boolean success = libraryManager.borrowBook(recordId, readerId, bookId, borrowDate);

        if (!success) {
            JOptionPane.showMessageDialog(this, "Khong the muon sach. Kiem tra ma phieu, nguoi muon, sach hoac so luong con lai.");
            return;
        }

        if (!saveDataSafely()) {
            return;
        }

        reloadBorrowTable();
        reloadBookTable(libraryManager.getBooks());
        reloadStatistics();
        clearBorrowForm();
        JOptionPane.showMessageDialog(this, "Muon sach thanh cong.");
    }

    private void returnBook() {
        String recordId = txtRecordId.getText().trim();
        String returnDate = txtReturnDate.getText().trim();

        if (recordId.isEmpty() || returnDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui long chon phieu muon va nhap ngay tra.");
            return;
        }

        boolean success = libraryManager.returnBook(recordId, returnDate);

        if (!success) {
            JOptionPane.showMessageDialog(this, "Khong the tra sach. Phieu muon khong ton tai hoac da duoc tra.");
            return;
        }

        if (!saveDataSafely()) {
            return;
        }
        
        reloadBorrowTable();
        reloadBookTable(libraryManager.getBooks());
        reloadStatistics();
        clearBorrowForm();
        JOptionPane.showMessageDialog(this, "Tra sach thanh cong.");
    }

    private void clearBorrowForm() {
        txtRecordId.setText("");
        txtBorrowDate.setText("");
        txtReturnDate.setText("");

        if (cmbBorrowReaderId.getItemCount() > 0) {
            cmbBorrowReaderId.setSelectedIndex(0);
        }

        if (cmbBorrowBookId.getItemCount() > 0) {
            cmbBorrowBookId.setSelectedIndex(0);
        }

        borrowTable.clearSelection();
    }

    private void fillBorrowFormFromSelectedRow() {
        int selectedRow = borrowTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        txtRecordId.setText(borrowTableModel.getValueAt(selectedRow, 0).toString());
        cmbBorrowReaderId.setSelectedItem(borrowTableModel.getValueAt(selectedRow, 1).toString());
        cmbBorrowBookId.setSelectedItem(borrowTableModel.getValueAt(selectedRow, 2).toString());
        txtBorrowDate.setText(borrowTableModel.getValueAt(selectedRow, 3).toString());
        txtReturnDate.setText(borrowTableModel.getValueAt(selectedRow, 4).toString());
    }

    private JPanel createStatisticPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel statisticContentPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        lblTotalBookTitles = new JLabel();
        lblTotalBookQuantity = new JLabel();
        lblBorrowedBookQuantity = new JLabel();
        lblBorrowingRecords = new JLabel();

        statisticContentPanel.add(new JLabel("Tong so dau sach:"));
        statisticContentPanel.add(lblTotalBookTitles);

        statisticContentPanel.add(new JLabel("Tong so ban sach:"));
        statisticContentPanel.add(lblTotalBookQuantity);

        statisticContentPanel.add(new JLabel("So sach dang duoc muon:"));
        statisticContentPanel.add(lblBorrowedBookQuantity);

        statisticContentPanel.add(new JLabel("So phieu muon chua tra:"));
        statisticContentPanel.add(lblBorrowingRecords);

        JButton btnRefresh = new JButton("Cap nhat");
        btnRefresh.addActionListener(e -> reloadStatistics());

        panel.add(statisticContentPanel, BorderLayout.NORTH);
        panel.add(btnRefresh, BorderLayout.SOUTH);

        reloadStatistics();

        return panel;
    }

    private void reloadStatistics() {
        int totalBookTitles = libraryManager.countBookTitles();
        int totalBookQuantity = libraryManager.countTotalBookQuantity();
        int availableBookQuantity = libraryManager.countAvailableBookQuantity();
        int borrowedBookQuantity = totalBookQuantity - availableBookQuantity;
        int borrowingRecords = libraryManager.countBorrowingRecords();

        lblTotalBookTitles.setText(String.valueOf(totalBookTitles));
        lblTotalBookQuantity.setText(String.valueOf(totalBookQuantity));
        lblBorrowedBookQuantity.setText(String.valueOf(borrowedBookQuantity));
        lblBorrowingRecords.setText(String.valueOf(borrowingRecords));
    }

    private boolean saveDataSafely() {
        try {
            libraryManager.saveData();
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Loi luu du lieu vao file: " + e.getMessage());
            return false;
        }
    }
}
