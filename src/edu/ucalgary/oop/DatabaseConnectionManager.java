package edu.ucalgary.oop;

import java.sql.Connection;
import java.sql.ResultSet;

public enum DatabaseConnectionManager {
    INSTANCE;

    public static final String DATABASE_URL = "jdbc:postgresql://localhost/project";
    public static final String USERNAME = "oop";
    public static final String PASSWORD = "ucalgary";
    private static Connection dbConnection;

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