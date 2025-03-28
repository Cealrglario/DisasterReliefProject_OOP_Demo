package edu.ucalgary.oop;

import java.util.List;

public class LocationAccess<U> implements DatabaseObjectAccess<Location, U> {
    @Override
    public List<Location> getAll() {
        return null;
    }

    @Override
    public Location getById(int id) {
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
