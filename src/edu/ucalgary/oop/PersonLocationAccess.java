package edu.ucalgary.oop;

import java.sql.SQLException;

public class PersonLocationAccess extends DatabaseAssociationAccess<Person, Location> {
    public Person[] getOccupantsOfLocation(Location location) throws SQLException {
        return null;
    }
}
