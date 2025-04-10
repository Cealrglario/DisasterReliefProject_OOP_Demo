package edu.ucalgary.oop;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InquiryAccess<U> extends DatabaseObjectAccess<Inquiry, U> {

    @Override
    public List<Inquiry> getAll() throws SQLException {
        List<Inquiry> retrievedInquiries = new ArrayList<>();

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT * FROM Inquiry");

        while(queryResults.next()) {
            int inquiryId = queryResults.getInt("inquiry_id");
            int inquirerId = queryResults.getInt("inquirer_id");
            int seekingId = queryResults.getInt("seeking_id");
            int locationId = queryResults.getInt("location_id");
            LocalDate dateOfInquiry = queryResults.getDate("date_of_inquiry").toLocalDate();
            String comments = queryResults.getString("comments");

            Inquiry retrievedInquiry = new Inquiry(inquiryId, inquirerId, seekingId, dateOfInquiry, comments);
            retrievedInquiry.setLastKnownLocationId(locationId);

            retrievedInquiries.add(retrievedInquiry);
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedInquiries;
    }

    @Override
    public Inquiry getById(int idOfInquiry) throws SQLException {
        Inquiry retrievedInquiry = null;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT * FROM Inquiry WHERE inquiry_id = " + idOfInquiry);

        if(queryResults.next()) {
            int inquiryId = queryResults.getInt("inquiry_id");
            int inquirerId = queryResults.getInt("inquirer_id");
            int seekingId = queryResults.getInt("seeking_id");
            int locationId = queryResults.getInt("location_id");
            LocalDate dateOfInquiry = queryResults.getDate("date_of_inquiry").toLocalDate();
            String comments = queryResults.getString("comments");

            retrievedInquiry = new Inquiry(inquiryId, inquirerId, seekingId, dateOfInquiry, comments);
            retrievedInquiry.setLastKnownLocationId(locationId);
        } else {
            throw new SQLException("Error getting Inquiry by ID: Inquiry doesn't exist.");
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedInquiry;
    }

    public Inquiry addInquiry(int inquirerId, int missingPersonId, LocalDate dateOfInquiry, String infoProvided) throws SQLException {
        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "INSERT INTO Inquiry (inquirer_id, seeking_id, date_of_inquiry, comments) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );

        myStmt.setInt(1, inquirerId);
        myStmt.setInt(2, missingPersonId);
        myStmt.setDate(3, java.sql.Date.valueOf(dateOfInquiry));
        myStmt.setString(4, infoProvided);

        int affectedRows = myStmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating inquiry failed, no rows affected.");
        }

        Inquiry newInquiry = null;
        try (ResultSet generatedKeys = myStmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int inquiryId = generatedKeys.getInt(1);

                newInquiry = new Inquiry(inquiryId, inquirerId, missingPersonId, dateOfInquiry, infoProvided);
            } else {
                throw new SQLException("Creating inquiry failed, couldn't obtain inquiry ID.");
            }
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return newInquiry;
    }

    @Override
    public boolean updateInfo(String infoToUpdate, U newInfo, int inquiryId) throws SQLException {
        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement("UPDATE Inquiry SET " + infoToUpdate + " = " + newInfo +
                " WHERE inquiry_id = ?");

        myStmt.setInt(1, inquiryId);

        int affectedRows = myStmt.executeUpdate();

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        if (affectedRows == 0) {
            System.out.println("Updating inquiry failed, no rows affected.");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public U getInfo(String infoToGet, int inquiryId) throws SQLException {
        U retrievedInfo;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT " + infoToGet + "FROM Inquiry WHERE inquiry_id = " +
                inquiryId);

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


    public boolean removeInquiry(Inquiry unwantedInquiry) throws SQLException {
        int unwantedInquiryId = unwantedInquiry.getInquiryId();

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement("DELETE FROM Inquiry WHERE inquiry_id = ?");

        myStmt.setInt(1, unwantedInquiryId);

        int rowsAffected = myStmt.executeUpdate();

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return rowsAffected > 0;
    }
}
