package edu.ucalgary.oop;

import java.sql.ResultSet;
import java.util.List;

public abstract class DatabaseObjectAccess<T, U> {
    private ResultSet queryResults;

    public ResultSet getQueryResults() {
        return this.queryResults;
    }

    public List<T> getAll() {
        return null;
    }

    public T getById(int idOfObject) {
        return null;
    }

    public boolean updateInfo(String infoToUpdate, U newInfo) {
        return false;
    }

    public U getInfo(String infoToGet, int idOfObject) {
        return null;
    }

    public boolean addEntry(T newEntry) {
        return false;
    }

    public boolean removeEntry(T exEntry) {
        return false;
    }

}
