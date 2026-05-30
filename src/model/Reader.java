package model;

import util.Searchable;

public class Reader extends Person implements Searchable {
    private String email;
    private String address;

    public Reader(String id, String name, String phone, String email, String address) {
        super(id, name, phone);
        this.email = email == null ? "" : email;
        this.address = address == null ? "" : address;
    }

    @Override
    public String getDisplayInfo() {
        return getId() + " - " + getName() + " - " + getPhone()
            + " - " + email + " - " + address;
    }

    @Override
    public boolean matchesKeyword(String keyword) {
        if (keyword == null) {
            return false;
        }

        String lowerKeyword = keyword.toLowerCase();
        
        return getId().toLowerCase().contains(lowerKeyword)
            || getName().toLowerCase().contains(lowerKeyword)
            || getPhone().toLowerCase().contains(lowerKeyword)
            || email.toLowerCase().contains(lowerKeyword)
            || address.toLowerCase().contains(lowerKeyword);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? "" : email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? "" : address;
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }
}