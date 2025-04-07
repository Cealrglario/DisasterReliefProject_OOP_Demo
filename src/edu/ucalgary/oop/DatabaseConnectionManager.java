package edu.ucalgary.oop;

import java.sql.Connection;
import java.sql.ResultSet;

public enum DatabaseConnectionManager {
    INSTANCE;

    public final String DATABASE_URL = "jdbc:postgresql://localhost/project";
    public final String USERNAME = "oop";
    public final String PASSWORD = "ucalgary";
    private Connection dbConnection;

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