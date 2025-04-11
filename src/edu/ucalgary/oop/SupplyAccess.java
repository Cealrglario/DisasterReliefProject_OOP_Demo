package edu.ucalgary.oop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplyAccess<U> extends DatabaseObjectAccess<Supply, U> {

    @Override
    public List<Supply> getAll() throws SQLException {
        List<Supply> retrievedSupplies = new ArrayList<>();

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT * FROM Supply");

        while(queryResults.next()) {
            int supplyId = queryResults.getInt("supply_id");
            String type = queryResults.getString("type");
            String comments = queryResults.getString("comments");

            Supply retrievedSupply = SupplyFactory.createSupply(supplyId, type, comments);

            retrievedSupplies.add(retrievedSupply);
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedSupplies;
    }


    @Override
    public Supply getById(int idOfSupply) throws SQLException {
        Supply retrievedSupply = null;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT * FROM Supply WHERE supply_id = " + idOfSupply);

        if(queryResults.next()) {
            int supplyId = queryResults.getInt("supply_id");
            String type = queryResults.getString("type");
            String comments = queryResults.getString("comments");

            retrievedSupply = SupplyFactory.createSupply(supplyId, type, comments);
        } else {
            System.out.println("Error getting Supply by ID: Supply doesn't exist.");
            myStmt.close();
            dbConnectionManager.closeDbConnection();
            return null;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedSupply;
    }


    public Supply addSupply(String type, String comments) throws SQLException {
        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "INSERT INTO Supply (type, comments) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

        myStmt.setString(1, type);
        myStmt.setString(2, comments);

        int affectedRows = myStmt.executeUpdate();

        if (affectedRows == 0) {
            System.out.println("Creating supply failed, no rows affected.");
            myStmt.close();
            dbConnectionManager.closeDbConnection();
            return null;
        }

        Supply newSupply = null;
        try (ResultSet generatedKeys = myStmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int supplyId = generatedKeys.getInt(1);
                newSupply = SupplyFactory.createSupply(supplyId, type, comments);
            } else {
                System.out.println("Creating supply failed, couldn't obtain supply ID.");
                myStmt.close();
                dbConnectionManager.closeDbConnection();
                return null;
            }
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return newSupply;
    }


    @Override
    public boolean updateInfo(String infoToUpdate, U newInfo, int supplyId) throws SQLException {
        int affectedRows;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement("UPDATE Supply SET " + infoToUpdate + " = ? WHERE supply_id = ?");

        myStmt.setObject(1, newInfo);
        myStmt.setInt(2, supplyId);

        try {
            affectedRows = myStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Updating supply failed: " + e.getMessage());
            myStmt.close();
            dbConnectionManager.closeDbConnection();
            return false;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        if (affectedRows == 0) {
            System.out.println("Updating supply failed, no rows affected.");
            return false;
        } else {
            return true;
        }
    }


    @Override
    public U getInfo(String infoToGet, int supplyId) throws SQLException {
        U retrievedInfo;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT " + infoToGet + " FROM Supply WHERE supply_id = " +
                supplyId);

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


    public boolean removeSupply(Supply unwantedSupply) throws SQLException {
        int unwantedSupplyId = unwantedSupply.getSupplyId();

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement("DELETE FROM Supply WHERE supply_id = ?");

        myStmt.setInt(1, unwantedSupplyId);

        int rowsAffected = myStmt.executeUpdate();

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return rowsAffected > 0;
    }
}
