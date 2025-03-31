package edu.ucalgary.oop;

public class PersonLocationAccess<T> extends DatabaseAssociationAccess<T> {
    @Override
    public boolean updateInfo(T info) {
        return false;
    }

    @Override
    public boolean getInfo(T info) {
        return false;
    }
}
