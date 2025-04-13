package edu.ucalgary.oop;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonAccess<U> extends DatabaseObjectAccess<Person, U> {

    @Override
    public List<Person> getAll() throws SQLException {
        List<Person> retrievedPersons = new ArrayList<>();

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT * FROM Person");

        while(queryResults.next()) {
            int assignedId = queryResults.getInt("person_id");
            String firstName = queryResults.getString("first_name");
            String lastName = queryResults.getString("last_name");
            String gender = queryResults.getString("gender");

            // Persons may or may not have a stored birthday
            LocalDate dateOfBirth = null;

            if(queryResults.getDate("date_of_birth") != null) {
                dateOfBirth = queryResults.getDate("date_of_birth").toLocalDate();
            }

            String phoneNumber = queryResults.getString("phone_number");
            Integer familyGroupId = queryResults.getInt("family_group");

            boolean inFamilyGroup;

            Person retrievedPerson;
            if(dateOfBirth != null) {
                retrievedPerson = new Person(assignedId, firstName, gender, dateOfBirth, phoneNumber);
            } else {
                retrievedPerson = new Person(assignedId, firstName, gender, phoneNumber);
            }

            if(familyGroupId != null) {
                inFamilyGroup = true;
                retrievedPerson.setFamilyGroupId(familyGroupId);
            } else {
                inFamilyGroup = false;
                retrievedPerson.setFamilyGroupId(null);
            }

            retrievedPerson.setLastName(lastName);
            retrievedPerson.setInFamilyGroup(inFamilyGroup);

            retrievedPersons.add(retrievedPerson);
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedPersons;
    }


    @Override
    public Person getById(int idOfPerson) throws SQLException {
        Person retrievedPerson = null;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT * FROM Person WHERE person_id = " + idOfPerson);

        if(queryResults.next()) {
            int assignedId = queryResults.getInt("person_id");
            String firstName = queryResults.getString("first_name");
            String lastName = queryResults.getString("last_name");
            String gender = queryResults.getString("gender");
            LocalDate dateOfBirth = null;
            if(queryResults.getDate("date_of_birth") != null) {
                dateOfBirth = queryResults.getDate("date_of_birth").toLocalDate();
            }
            String phoneNumber = queryResults.getString("phone_number");
            Integer familyGroupId = queryResults.getInt("family_group");

            boolean inFamilyGroup;

            if(dateOfBirth != null) {
                retrievedPerson = new Person(assignedId, firstName, gender, dateOfBirth, phoneNumber);
            } else {
                retrievedPerson = new Person(assignedId, firstName, gender, phoneNumber);
            }

            if(familyGroupId != null) {
                inFamilyGroup = true;
                retrievedPerson.setFamilyGroupId(familyGroupId);
            } else {
                inFamilyGroup = false;
                retrievedPerson.setFamilyGroupId(null);
            }

            retrievedPerson.setLastName(lastName);
            retrievedPerson.setInFamilyGroup(inFamilyGroup);
        } else {
            System.out.println("Error getting Person by ID: Person doesn't exist.");
            myStmt.close();
            dbConnectionManager.closeDbConnection();
            return null;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedPerson;
    }


    public Person addPerson(String firstName, String gender, LocalDate dateOfBirth, String phoneNumber) throws SQLException {
        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "INSERT INTO Person (first_name, gender, date_of_birth, phone_number) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );

        myStmt.setString(1, firstName);
        myStmt.setString(2, gender);
        if(dateOfBirth != null) {
            myStmt.setDate(3, java.sql.Date.valueOf(dateOfBirth));
        } else {
            myStmt.setNull(3, Types.DATE);
        }
        myStmt.setString(4, phoneNumber);

        int affectedRows = myStmt.executeUpdate();
        if (affectedRows == 0) {
            System.out.println("Creating person failed, no rows affected.");
            myStmt.close();
            dbConnectionManager.closeDbConnection();
            return null;
        }

        Person newPerson;
        try (ResultSet generatedKeys = myStmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int assignedId = generatedKeys.getInt(1);
                if(dateOfBirth != null) {
                    newPerson = new Person(assignedId, firstName, gender, dateOfBirth, phoneNumber);
                } else {
                    newPerson = new Person(assignedId, firstName, gender, phoneNumber);
                }
            } else {
                System.out.println("Creating person failed, couldn't obtain person ID.");
                myStmt.close();
                dbConnectionManager.closeDbConnection();
                return null;
            }
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return newPerson;
    }


    @Override
    public boolean updateInfo(String infoToUpdate, U newInfo, int personId) throws SQLException {
        int affectedRows;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement("UPDATE Person SET " + infoToUpdate + " = ? WHERE person_id = ?");

        myStmt.setObject(1, newInfo);
        myStmt.setInt(2, personId);

        try {
            affectedRows = myStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Updating person failed: " + e.getMessage());
            myStmt.close();
            dbConnectionManager.closeDbConnection();
            return false;
        }
        myStmt.close();
        dbConnectionManager.closeDbConnection();

        if (affectedRows == 0) {
            System.out.println("Updating person failed, no rows affected.");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public U getInfo(String infoToGet, int personId) throws SQLException {
        U retrievedInfo;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT " + infoToGet + " FROM Person WHERE person_id = " +
                personId);

        if(queryResults.next()) {
            retrievedInfo = (U) queryResults.getObject(infoToGet);
        } else {
            System.out.println("Error retrieving info, results empty.");
            myStmt.close();
            dbConnectionManager.closeDbConnection();
            return null;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedInfo;
    }



    public boolean removePerson(Person unwantedPerson) throws SQLException {
        int unwantedPersonId = unwantedPerson.getAssignedId();

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement("DELETE FROM Person WHERE person_id = ?");

        myStmt.setInt(1, unwantedPersonId);

        int rowsAffected = myStmt.executeUpdate();

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return rowsAffected > 0;
    }
}
