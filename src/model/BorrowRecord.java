package model;

public class BorrowRecord {
    private String recordId;
    private String bookId;
    private String readerId;
    private String borrowDate;
    private String returnDate;
    private boolean returned;

    public BorrowRecord(String recordId, String bookId, String readerId, String borrowDate) {
        this.recordId = recordId;
        this.bookId = bookId;
        this.readerId = readerId;
        this.borrowDate = borrowDate;
        this.returnDate = "";
        this.returned = false;
    }

    public BorrowRecord(String recordId, String bookId, String readerId, String borrowDate, String returnDate, boolean returned) {
        this.recordId = recordId;
        this.bookId = bookId;
        this.readerId = readerId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate == null ? "" : returnDate;
        this.returned = returned;
    }

    public boolean markReturned(String returnDate) {
        if (returned) {
            return false;
        }

        this.returnDate = returnDate;
        this.returned = true;
        return true;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return returned;
    }

    @Override
    public String toString() {
        return recordId + " - " + readerId + " - " + bookId
                + " - " + borrowDate + " - " + returnDate
                + " - " + (returned ? "Da tra" : "Dang muon");
    }
}