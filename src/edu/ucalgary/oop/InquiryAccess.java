package edu.ucalgary.oop;

import java.util.List;

public class InquiryAccess<U> extends DatabaseObjectAccess<Inquiry, U> {
    @Override
    public List<Inquiry> getAll() {
        return null;
    }

    @Override
    public Inquiry getById(int id) {
        return null;
    }

    @Override
    public boolean updateInfo(U info) {
        return false;
    }

    @Override
    public boolean getInfo(U info) {
        return false;
    }
}
