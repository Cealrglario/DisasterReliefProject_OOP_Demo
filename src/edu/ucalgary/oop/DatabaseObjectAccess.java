package edu.ucalgary.oop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class DatabaseObjectAccess<T, U> {
    protected ResultSet queryResults;
    protected DatabaseConnectionManager dbConnectionManager = DatabaseConnectionManager.INSTANCE;

    public ResultSet getQueryResults() {
        return this.queryResults;
    }

    public List<T> getAll() throws SQLException {
        return null;
    }

    public T getById(int idOfObject) throws SQLException {
        return null;
    }

    public boolean updateInfo(String infoToUpdate, U newInfo, int objectId) throws SQLException {
        return false;
    }

    public U getInfo(String infoToGet, int objectId) throws SQLException {
        return null;
    }
}
