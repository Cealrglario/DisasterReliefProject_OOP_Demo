package edu.ucalgary.oop;

import java.sql.SQLException;
import java.util.List;

public enum LocationService {
    INSTANCE;

    private final LocationAccess<Object> locationAccess = new LocationAccess<>();
    private final PersonLocationAccess personLocationAccess = new PersonLocationAccess();

    public Location getLocation(int locationId) throws SQLException {
        return locationAccess.getById(locationId);
    }

    public Location getLocationWithOccupants(int locationId) throws SQLException {
        Location retrievedLocation = locationAccess.getById(locationId);
        if (retrievedLocation == null) {
            return null;
        }

        List<Person> occupants = personLocationAccess.getOccupantsOfLocation(retrievedLocation);
        retrievedLocation.setOccupants(occupants);

        return retrievedLocation;
    }

    public List<Location> getAllLocationsWithOccupants() throws SQLException {
        List<Location> retrievedLocations = locationAccess.getAll();

        for (int i = 0; i < retrievedLocations.size(); i++) {
            Location locationWithOccupant = getLocationWithOccupants(retrievedLocations.get(i).getLocationId());
            retrievedLocations.set(i, locationWithOccupant);
        }

        return retrievedLocations;
    }

    public boolean updateLocationName(Location location, String newName) throws SQLException {
        location.setName(newName);
        return locationAccess.updateInfo("name", newName, location.getLocationId());
    }

    public boolean updateLocationAddress(Location location, String newAddress) throws SQLException {
        location.setAddress(newAddress);
        return locationAccess.updateInfo("address", newAddress, location.getLocationId());
    }

    public boolean addOccupant(Location location, Person person) throws SQLException {
        location.addOccupant(person);
        return personLocationAccess.addEntry(person, location);
    }

    public boolean removeOccupant(Location location, Person person) throws SQLException {
        location.removeOccupant(person);
        return personLocationAccess.removeEntry(person, location);
    }

    public void refreshOccupants(Location location) throws SQLException {
        List<Person> occupants = personLocationAccess.getOccupantsOfLocation(location);
        location.setOccupants(occupants);
    }
}
