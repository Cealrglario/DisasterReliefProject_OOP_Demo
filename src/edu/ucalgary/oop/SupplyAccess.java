package edu.ucalgary.oop;

import java.util.List;

public class SupplyAccess<U> implements DatabaseObjectAccess<Supply, U> {
    @Override
    public List<Supply> getAll() {
        return null;
    }

    @Override
    public Supply getById(int id) {
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
