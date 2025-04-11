package edu.ucalgary.oop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class DatabaseAssociationAccess<T, U> {
    protected ResultSet queryResults;
    protected DatabaseConnectionManager dbConnectionManager = DatabaseConnectionManager.INSTANCE;

    public ResultSet getQueryResults() {
        return this.queryResults;
    }
}
