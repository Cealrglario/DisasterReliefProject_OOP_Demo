package edu.ucalgary.oop;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SupplyPersonAllocationAccess extends DatabaseAssociationAccess<Supply, Person> {

    public List<Allocation> getAll() throws SQLException {
        List<Allocation> retrievedAllocations = new ArrayList<>();
        SupplyService supplyService = SupplyService.INSTANCE;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT * FROM SupplyAllocation WHERE location_id IS NULL");

        while(queryResults.next()) {
            Supply retrievedSupply;
            LocalDate retrievedAllocationDate;

            int retrievedSupplyId = queryResults.getInt("supply_id");
            int retrievedPersonId = queryResults.getInt("person_id");
            retrievedAllocationDate = queryResults.getDate("allocation_date").toLocalDate();

            retrievedSupply = supplyService.getSupplyById(retrievedSupplyId);

            Allocation retrievedAllocation = new Allocation(retrievedSupply, retrievedPersonId,
                    null, retrievedAllocationDate);

            retrievedAllocations.add(retrievedAllocation);
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedAllocations;
    }


    public Allocation getById(Supply supply, Person person) throws SQLException {
        Allocation retrievedAllocation = null;
        SupplyService supplyService = SupplyService.INSTANCE;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement("SELECT * FROM SupplyAllocation WHERE location_id IS NULL " +
                "AND supply_id = ? AND person_id = ?");

        myStmt.setInt(1, supply.getSupplyId());
        myStmt.setInt(2, person.getAssignedId());

        queryResults = myStmt.executeQuery();

        if(queryResults.next()) {
            Supply retrievedSupply;
            LocalDate retrievedAllocationDate;

            int retrievedSupplyId = queryResults.getInt("supply_id");
            int retrievedPersonId = queryResults.getInt("person_id");
            retrievedAllocationDate = queryResults.getDate("allocation_date").toLocalDate();

            retrievedSupply = supplyService.getSupplyById(retrievedSupplyId);

            retrievedAllocation = new Allocation(retrievedSupply, retrievedPersonId,
                    null, retrievedAllocationDate);
        } else {
            System.out.println("Error getting SupplyAllocation by ID: Association doesn't exist.");
            return null;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedAllocation;
    }


    public Allocation addEntry(Supply supply, Person person, LocalDate allocationDate) throws SQLException {
        if (supply == null || person == null) {
            return null;
        }

        Allocation addedAllocation = new Allocation(supply, person.getAssignedId(), null, allocationDate);
        int affectedRows;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "INSERT INTO SupplyAllocation (supply_id, person_id, location_id, allocation_date) VALUES (?, ?, NULL, ?)"
        );

        myStmt.setInt(1, supply.getSupplyId());
        myStmt.setInt(2, person.getAssignedId());
        myStmt.setDate(3, Date.valueOf(allocationDate));

        try {
            affectedRows = myStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Adding supply-person allocation failed: " + e.getMessage());
            myStmt.close();
            dbConnectionManager.closeDbConnection();
            return null;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        if (affectedRows == 0) {
            System.out.println("Adding supply-person allocation failed, no rows affected.");
            return null;
        } else {
            return addedAllocation;
        }
    }


    public boolean removeEntry(Supply supply, Person person, LocalDate allocationDate) throws SQLException {
        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "DELETE FROM SupplyAllocation WHERE supply_id = ? AND person_id = ? AND allocation_date = ? AND location_id IS NULL"
        );

        myStmt.setInt(1, supply.getSupplyId());
        myStmt.setInt(2, person.getAssignedId());
        myStmt.setDate(3, Date.valueOf(allocationDate));

        int rowsAffected = myStmt.executeUpdate();

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return rowsAffected > 0;
    }

    public List<Supply> getPersonSupplies(Person person) throws SQLException {
        List<Supply> supplies = new ArrayList<>();
        SupplyService supplyService = SupplyService.INSTANCE;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "SELECT supply_id FROM SupplyAllocation WHERE person_id = ? AND location_id IS NULL"
        );

        myStmt.setInt(1, person.getAssignedId());

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
