package edu.ucalgary.oop;

import java.util.List;

public class InquiryAccess<U, V, W> extends DatabaseObjectAccess<Inquiry, U, V, W> {
    @Override
    public List<Inquiry> getAll() {
        return null;
    }

    @Override
    public Inquiry getById(int id) {
        return null;
    }

    @Override
    public boolean updateInfo(U infoToUpdate, V newInfo) {
        return false;
    }

    @Override
    public W getInfo(U infoToGet, W infoType) {
        return null;
    }

    @Override
    public boolean addEntry(Inquiry newEntry) {
        return false;
    }

    @Override
    public boolean removeEntry(Inquiry exEntry) {
        return false;
    }
}
