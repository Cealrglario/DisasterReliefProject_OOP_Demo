package edu.ucalgary.oop;

import java.util.List;

public class SupplyAccess<U, V, W> extends DatabaseObjectAccess<Supply, U, V, W> {
    @Override
    public List<Supply> getAll() {
        return null;
    }

    @Override
    public Supply getById(int id) {
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
    public boolean addEntry(Supply newEntry) {
        return false;
    }

    @Override
    public boolean removeEntry(Supply exEntry) {
        return false;
    }
}
