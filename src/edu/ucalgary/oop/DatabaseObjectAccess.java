package edu.ucalgary.oop;

import java.sql.ResultSet;
import java.util.List;

public abstract class DatabaseObjectAccess<T, U> {
    private ResultSet queryResults;

    public List<T> getAll() {
        return null;
    }

    public T getById(int id) {
        return null;
    }

    public boolean updateInfo(U info) {
        return false;
    }

    public boolean getInfo(U info) {
        return false;
    }


}
