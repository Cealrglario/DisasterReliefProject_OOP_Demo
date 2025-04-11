package edu.ucalgary.oop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationAccess<U> extends DatabaseObjectAccess<Location, U> {

    @Override
    public List<Location> getAll() throws SQLException {
        List<Location> retrievedLocations = new ArrayList<>();

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT * FROM Location");

        while(queryResults.next()) {
            int locationId = queryResults.getInt("location_id");
            String name = queryResults.getString("name");
            String address = queryResults.getString("address");

            Location retrievedLocation = new Location(locationId, name, address);
            retrievedLocations.add(retrievedLocation);
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedLocations;
    }


    @Override
    public Location getById(int idOfLocation) throws SQLException {
        Location retrievedLocation = null;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT * FROM Location WHERE location_id = " + idOfLocation);

        if(queryResults.next()) {
            int locationId = queryResults.getInt("location_id");
            String name = queryResults.getString("name");
            String address = queryResults.getString("address");

            retrievedLocation = new Location(locationId, name, address);
        } else {
            System.out.println("Error getting Location by ID: Location doesn't exist.");
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedLocation;
    }


    @Override
    public boolean updateInfo(String infoToUpdate, U newInfo, int locationId) throws SQLException {
        int affectedRows;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement("UPDATE Location SET " + infoToUpdate + " = ? WHERE location_id = ?");

        myStmt.setObject(1, newInfo);
        myStmt.setInt(2, locationId);

        try {
            affectedRows = myStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Updating Location failed: " + e.getMessage());
            return false;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        if (affectedRows == 0) {
            System.out.println("Updating location failed, no rows affected.");
            return false;
        } else {
            return true;
        }
    }


    @Override
    public U getInfo(String infoToGet, int locationId) throws SQLException {
        U retrievedInfo;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT " + infoToGet + " FROM Location WHERE location_id = " +
                locationId);

        if(queryResults.next()) {
            retrievedInfo = (U) queryResults.getObject(infoToGet);
        } else {
            System.out.println("Error retrieving info, results empty.");
            return null;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedInfo;
    }
}
