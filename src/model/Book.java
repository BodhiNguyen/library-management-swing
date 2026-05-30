package model;

import util.Searchable;

public class Book implements Searchable {
    private String bookId;
    private String title;
    private String author;
    private String category;
    private int quantity;
    private int availableQuantity;

    public Book(String bookId, String title, String author, String category, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("So luong sach khong duoc am");
        }

        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        setQuantity(quantity);
        this.availableQuantity = quantity;
    }

    public Book(String bookId, String title, String author, String category, int quantity, int availableQuantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("So luong sach khong duoc am");
        }

        if (availableQuantity < 0 || availableQuantity > quantity) {
            throw new IllegalArgumentException("So luong sach con lai khong hop le");
        }

        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
    }

    public boolean borrowOne() {
        if (availableQuantity <= 0) {
            return false;
        }

        availableQuantity--;
        return true;
    }

    public boolean returnOne() {
        if (availableQuantity >= quantity) {
            return false;
        }

        availableQuantity++;
        return true;
    }

    @Override
    public boolean matchesKeyword(String keyword) {
        if (keyword == null) {
            return false;
        }

        String lowerKeyword = keyword.toLowerCase();

        return bookId.toLowerCase().contains(lowerKeyword)
            || title.toLowerCase().contains(lowerKeyword)
            || author.toLowerCase().contains(lowerKeyword)
            || category.toLowerCase().contains(lowerKeyword);
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Số lượng không thể âm.");
        }

        int borrowingQuantity = this.quantity - this.availableQuantity;
        if (quantity < borrowingQuantity) {
            throw new IllegalArgumentException("So luong moi khong duoc nho hon so sach dang muon.");
        }

        this.quantity = quantity;
        this.availableQuantity = quantity - borrowingQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    @Override
    public String toString() {
        return bookId + "  - " + title + " - " + author 
            + " - " + category + " - " + availableQuantity + "/" + quantity;
    }
}
