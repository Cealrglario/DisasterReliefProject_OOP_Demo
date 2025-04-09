package edu.ucalgary.oop;

import java.sql.Connection;
import java.sql.*;

public enum DatabaseConnectionManager {
    INSTANCE;

    public final String DATABASE_URL = "jdbc:postgresql://localhost/ensf380project";
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
        try {
            this.dbConnection = DriverManager.getConnection(this.DATABASE_URL, this.USERNAME, this.PASSWORD);

        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    public void closeDbConnection() {
        try {
            if (!this.dbConnection.isClosed()) {
                this.dbConnection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing database connection: " + e.getMessage());
        }
    }

}