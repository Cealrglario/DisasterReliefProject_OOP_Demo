package edu.ucalgary.oop;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonLocationAccess extends DatabaseAssociationAccess {

    public Map<Integer, Integer> getAll() throws SQLException {
        Map<Integer, Integer> retrievedAssociations = new HashMap<>();

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT * FROM PersonLocation");

        PersonAccess<Object> personAccess = new PersonAccess<>();
        LocationAccess<Object> locationAccess = new LocationAccess<>();

        while(queryResults.next()) {
            int personId = queryResults.getInt("person_id");
            int locationId = queryResults.getInt("location_id");

            retrievedAssociations.put(personId, locationId);
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedAssociations;
    }


    public Map<Integer, Integer> getById(Person person, Location location) throws SQLException {
        Map<Integer, Integer> retrievedAssociation = null;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "SELECT * FROM PersonLocation WHERE person_id = ? AND location_id = ?"
        );

        myStmt.setInt(1, person.getAssignedId());
        myStmt.setInt(2, location.getLocationId());

        queryResults = myStmt.executeQuery();

        if(queryResults.next()) {
            retrievedAssociation = new HashMap<>();
            retrievedAssociation.put(person.getAssignedId(), location.getLocationId());
        } else {
            System.out.println("Error getting PersonLocation by IDs: Association doesn't exist.");
            myStmt.close();
            dbConnectionManager.closeDbConnection();
            return null;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedAssociation;
    }


    public boolean addEntry(Person person, Location location) throws SQLException {
        int affectedRows;
        PreparedStatement myStmt;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        if (person != null && location != null) {
            myStmt = dbConnect.prepareStatement("INSERT INTO PersonLocation (person_id, location_id) VALUES (?, ?)");

            myStmt.setInt(1, person.getAssignedId());
            myStmt.setInt(2, location.getLocationId());
        } else {
            dbConnectionManager.closeDbConnection();
            return false;
        }

        try {
            affectedRows = myStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Adding person-location association failed: " + e.getMessage());
            myStmt.close();
            dbConnectionManager.closeDbConnection();
            return false;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        if (affectedRows == 0) {
            System.out.println("Adding person-location association failed, no rows affected.");
            return false;
        } else {
            return true;
        }
    }


    public boolean removeEntry(Person person, Location location) throws SQLException {
        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement("DELETE FROM PersonLocation WHERE person_id = ? AND location_id = ?");

        myStmt.setInt(1, person.getAssignedId());
        myStmt.setInt(2, location.getLocationId());

        int rowsAffected = myStmt.executeUpdate();

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return rowsAffected > 0;
    }


    public List<Person> getOccupantsOfLocation(Location location) throws SQLException {
        List<Person> occupants = new ArrayList<>();

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement("SELECT person_id FROM PersonLocation WHERE location_id = ?");

        myStmt.setInt(1, location.getLocationId());

        queryResults = myStmt.executeQuery();

        PersonAccess<Object> personAccess = new PersonAccess<>();

        while(queryResults.next()) {
            int personId = queryResults.getInt("person_id");
            Person person = personAccess.getById(personId);
            occupants.add(person);
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return occupants;
    }
}
