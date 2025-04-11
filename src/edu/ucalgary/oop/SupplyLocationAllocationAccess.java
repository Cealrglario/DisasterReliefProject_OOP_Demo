package edu.ucalgary.oop;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplyLocationAllocationAccess extends DatabaseAssociationAccess<Supply, Location> {

    @Override
    public List<Map<Supply, Location>> getAll() throws SQLException {
        List<Map<Supply, Location>> retrievedAllocations = new ArrayList<>();
        SupplyService supplyService = SupplyService.INSTANCE;
        LocationService locationService = LocationService.INSTANCE;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT * FROM SupplyAllocation WHERE person_id IS NULL");

        while(queryResults.next()) {
            Supply retrievedSupply;
            Location retrievedLocation;
            Map<Supply, Location> retrievedAllocation = new HashMap<>();

            int retrievedSupplyId = queryResults.getInt("supply_id");
            int retrievedLocationId = queryResults.getInt("location_id");

            retrievedSupply = supplyService.getSupplyById(retrievedSupplyId);
            retrievedLocation = locationService.getLocation(retrievedLocationId);

            retrievedAllocation.put(retrievedSupply, retrievedLocation);
            retrievedAllocations.add(retrievedAllocation);
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedAllocations;
    }


    @Override
    public Map<Supply, Location> getById(Supply supply, Location location) throws SQLException {
        Map<Supply, Location> retrievedAllocation = new HashMap<>();
        SupplyService supplyService = SupplyService.INSTANCE;
        LocationService locationService = LocationService.INSTANCE;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement("SELECT * FROM SupplyAllocation WHERE person_id IS NULL " +
                "AND supply_id = ? AND location_id = ?");

        myStmt.setInt(1, supply.getSupplyId());
        myStmt.setInt(2, location.getLocationId());

        queryResults = myStmt.executeQuery();

        if(queryResults.next()) {
            Supply retrievedSupply;
            Location retrievedLocation;

            int retrievedSupplyId = queryResults.getInt("supply_id");
            int retrievedLocationId = queryResults.getInt("location_id");

            retrievedSupply = supplyService.getSupplyById(retrievedSupplyId);
            retrievedLocation = locationService.getLocation(retrievedLocationId);

            retrievedAllocation.put(retrievedSupply, retrievedLocation);
        } else {
            System.out.println("Error getting SupplyAllocation by IDs: Association doesn't exist.");
            return null;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedAllocation;
    }


    @Override
    public boolean addEntry(Supply supply, Location location) throws SQLException {
        if (supply == null || location == null) {
            return false;
        }

        int affectedRows;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "INSERT INTO SupplyAllocation (supply_id, person_id, location_id) VALUES (?, NULL, ?)"
        );

        myStmt.setInt(1, supply.getSupplyId());
        myStmt.setInt(2, location.getLocationId());

        try {
            affectedRows = myStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Adding supply-location allocation failed: " + e.getMessage());
            myStmt.close();
            dbConnectionManager.closeDbConnection();
            return false;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        if (affectedRows == 0) {
            System.out.println("Adding supply-location allocation failed, no rows affected.");
            return false;
        } else {
            return true;
        }
    }


    @Override
    public boolean removeEntry(Supply supply, Location location) throws SQLException {
        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "DELETE FROM SupplyAllocation WHERE supply_id = ? AND location_id = ? AND person_id IS NULL"
        );

        myStmt.setInt(1, supply.getSupplyId());
        myStmt.setInt(2, location.getLocationId());

        int rowsAffected = myStmt.executeUpdate();

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return rowsAffected > 0;
    }


    public List<Supply> getSuppliesAtLocation(Location location) throws SQLException {
        List<Supply> supplies = new ArrayList<>();
        SupplyService supplyService = SupplyService.INSTANCE;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "SELECT supply_id FROM SupplyAllocation WHERE location_id = ? AND person_id IS NULL"
        );

        myStmt.setInt(1, location.getLocationId());

        queryResults = myStmt.executeQuery();

        while(queryResults.next()) {
            int supplyId = queryResults.getInt("supply_id");
            Supply supply = supplyService.getSupplyById(supplyId);
            supplies.add(supply);
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return supplies;
    }
}
