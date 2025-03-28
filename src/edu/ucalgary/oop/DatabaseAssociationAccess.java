package edu.ucalgary.oop;

public interface DatabaseAssociationAccess<T> {
    public boolean updateInfo(T info);
    public boolean getInfo(T info);
}
