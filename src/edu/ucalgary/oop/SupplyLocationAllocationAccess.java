package edu.ucalgary.oop;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SupplyLocationAllocationAccess extends DatabaseAssociationAccess<Supply, Location> {

    public List<Allocation> getAll() throws SQLException {
        List<Allocation> retrievedAllocations = new ArrayList<>();
        SupplyService supplyService = SupplyService.INSTANCE;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT * FROM SupplyAllocation WHERE person_id IS NULL");

        while(queryResults.next()) {
            Supply retrievedSupply;
            LocalDate retrievedAllocationDate;

            int retrievedSupplyId = queryResults.getInt("supply_id");
            int retrievedLocationId = queryResults.getInt("location_id");
            retrievedAllocationDate = queryResults.getDate("allocation_date").toLocalDate();

            retrievedSupply = supplyService.getSupplyById(retrievedSupplyId);

            Allocation retrievedAllocation = new Allocation(retrievedSupply, null,
                    retrievedLocationId, retrievedAllocationDate);

            retrievedAllocations.add(retrievedAllocation);
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedAllocations;
    }


    public Allocation getById(Supply supply, Location location) throws SQLException {
        Allocation retrievedAllocation = null;
        SupplyService supplyService = SupplyService.INSTANCE;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement("SELECT * FROM SupplyAllocation WHERE person_id IS NULL " +
                "AND supply_id = ? AND location_id = ?");

        myStmt.setInt(1, supply.getSupplyId());
        myStmt.setInt(2, location.getLocationId());

        queryResults = myStmt.executeQuery();

        if(queryResults.next()) {
            Supply retrievedSupply;
            LocalDate retrievedAllocationDate;

            int retrievedSupplyId = queryResults.getInt("supply_id");
            int retrievedLocationId = queryResults.getInt("location_id");
            retrievedAllocationDate = queryResults.getDate("allocation_date").toLocalDate();

            retrievedSupply = supplyService.getSupplyById(retrievedSupplyId);

            retrievedAllocation = new Allocation(retrievedSupply, null,
                    retrievedLocationId, retrievedAllocationDate);
        } else {
            System.out.println("Error getting SupplyAllocation by ID: Association doesn't exist.");
            return null;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedAllocation;
    }


    public Allocation addEntry(Supply supply, Location location, LocalDate allocationDate) throws SQLException {
        if (supply == null || location == null) {
            return null;
        }

        Allocation addedAllocation = new Allocation(supply, null, location.getLocationId(), allocationDate);
        int affectedRows;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "INSERT INTO SupplyAllocation (supply_id, person_id, location_id, allocation_date) VALUES (?, NULL, ?, ?)"
        );

        myStmt.setInt(1, supply.getSupplyId());
        myStmt.setInt(2, location.getLocationId());
        myStmt.setDate(3, Date.valueOf(allocationDate));

        try {
            affectedRows = myStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Adding supply-location allocation failed: " + e.getMessage());
            myStmt.close();
            dbConnectionManager.closeDbConnection();
            return null;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        if (affectedRows == 0) {
            System.out.println("Adding supply-location allocation failed, no rows affected.");
            return null;
        } else {
            return addedAllocation;
        }
    }


    public boolean removeEntry(Supply supply, Location location, LocalDate allocationDate) throws SQLException {
        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "DELETE FROM SupplyAllocation WHERE supply_id = ? AND location_id = ? AND allocation_date = ? AND person_id IS NULL"
        );

        myStmt.setInt(1, supply.getSupplyId());
        myStmt.setInt(2, location.getLocationId());
        myStmt.setDate(3, Date.valueOf(allocationDate));

        int rowsAffected = myStmt.executeUpdate();

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return rowsAffected > 0;
    }


    public List<Allocation> getSuppliesAtLocation(Location location) throws SQLException {
        List<Allocation> allocations = new ArrayList<>();
        SupplyService supplyService = SupplyService.INSTANCE;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "SELECT supply_id, allocation_date FROM SupplyAllocation WHERE location_id = ? AND person_id IS NULL"
        );

        myStmt.setInt(1, location.getLocationId());

        queryResults = myStmt.executeQuery();

        while(queryResults.next()) {
            int supplyId = queryResults.getInt("supply_id");
            int locationId = location.getLocationId();
            LocalDate dateAllocated = queryResults.getDate("allocation_date").toLocalDate();

            Supply retrievedSupply = supplyService.getSupplyById(supplyId);

            Allocation retrievedAllocation = new Allocation(retrievedSupply, null, locationId, dateAllocated);
            allocations.add(retrievedAllocation);
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return allocations;
    }
}
