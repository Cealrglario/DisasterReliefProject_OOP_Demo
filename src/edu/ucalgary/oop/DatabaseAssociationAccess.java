package edu.ucalgary.oop;

import java.sql.ResultSet;

public abstract class DatabaseAssociationAccess<T, U, V> {
    private ResultSet queryResults;

    public ResultSet getQueryResults() {
        return this.queryResults;
    }

    public boolean updateInfo(T infoToUpdate, V newInfo) {
        return false;
    }

    public boolean getInfo(T infoToGet) {
        return false;
    }

    public boolean addEntry(T entry1, U entry2) {
        return false;
    }

    boolean removeEntry(T entry1, U entry2) {
        return false;
    }
}
