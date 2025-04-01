package edu.ucalgary.oop;

import java.sql.ResultSet;

public abstract class DatabaseAssociationAccess<T, U, V, W> {
    private ResultSet queryResults;

    public ResultSet getQueryResults() {
        return this.queryResults;
    }

    public boolean getAll() { return false; }

    public boolean getById(T entry1, U entry2) { return false; }

    public boolean updateInfo(T infoToUpdate, V newInfo) {
        return false;
    }

    public W getInfo(T infoToGet, W infoType) {
        return null;
    }

    public boolean addEntry(T entry1, U entry2) {
        return false;
    }

    public boolean removeEntry(T entry1, U entry2) {
        return false;
    }
}
