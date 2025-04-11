package edu.ucalgary.oop;

import java.sql.*;
import java.sql.SQLException;
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
        queryResults = myStmt.executeQuery("SELECT * FROM SupplyAllocation WHERE person_id = NULL");

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

        PreparedStatement myStmt = dbConnect.prepareStatement("SELECT * FROM SupplyAllocation WHERE person_id = NULL " +
                "AND supply_id = ? AND location_id = ?" );

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
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedAllocation;
    }
}