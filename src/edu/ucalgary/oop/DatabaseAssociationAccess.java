package edu.ucalgary.oop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class DatabaseAssociationAccess<T, U> {
    private ResultSet queryResults;

    public ResultSet getQueryResults() {
        return this.queryResults;
    }

    public List<Map<T, U>> getAll() throws SQLException { return null; }

    public Map<T, U> getById(T entry1, U entry2) throws SQLException { return null; }

    public boolean addEntry(T entry1, U entry2) throws SQLException {
        return false;
    }

    public boolean removeEntry(T entry1, U entry2) throws SQLException {
        return false;
    }
}
