package edu.ucalgary.oop;

import java.sql.ResultSet;
import java.util.List;

public abstract class DatabaseObjectAccess<T, U, V, W> {
    private ResultSet queryResults;

    public ResultSet getQueryResults() {
        return this.queryResults;
    }

    public List<T> getAll() {
        return null;
    }

    public T getById(int id) {
        return null;
    }

    public boolean updateInfo(U infoToUpdate, V newInfo) {
        return false;
    }

    public W getInfo(U infoToGet, W infoType) {
        return null;
    }

    public boolean addEntry(T newEntry) {
        return false;
    }

    public boolean removeEntry(T exEntry) {
        return false;
    }

}
