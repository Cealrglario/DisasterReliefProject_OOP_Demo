package edu.ucalgary.oop;

import java.sql.Connection;
import java.sql.ResultSet;

public enum DatabaseConnectionManager {
    INSTANCE;

    private static final String DATABASE_URL = "jdbc:postgresql://localhost/project";
    private static final String USERNAME = "oop";
    private static final String PASSWORD = "ucalgary";
    private static Connection dbConnection;
    private static ResultSet queryResults;

    public String getDatabaseUrl() {
        return this.DATABASE_URL;
    }

    public String getUsername() {
        return this.USERNAME;
    }

    public String getPassword() {
        return this.PASSWORD;
    }

    public Connection getDbConnection() {
        return this.dbConnection;
    }

    public void initializeDbConnection() {

    }

    public void closeDbConnection() {

    }

}