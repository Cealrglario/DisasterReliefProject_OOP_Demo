package edu.ucalgary.oop;

import java.util.List;

public class LocationAccess<U, V, W> extends DatabaseObjectAccess<Location, U, V, W> {
    @Override
    public List<Location> getAll() {
        return null;
    }

    @Override
    public Location getById(int id) {
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
    public boolean addEntry(Location newEntry) {
        return false;
    }

    @Override
    public boolean removeEntry(Location exEntry) {
        return false;
    }
}
