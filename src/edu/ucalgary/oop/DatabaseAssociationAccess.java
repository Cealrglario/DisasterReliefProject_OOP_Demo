package edu.ucalgary.oop;

import java.sql.ResultSet;

public abstract class DatabaseAssociationAccess<T> {
    private ResultSet queryResults;

    public boolean updateInfo(T info) {
        return false;
    }

    public boolean getInfo(T info) {
        return false;
    }
}
