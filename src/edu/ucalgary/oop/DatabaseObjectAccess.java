package edu.ucalgary.oop;

import java.util.List;

public interface DatabaseObjectAccess<T, U> {
    public List<T> getAll();
    public T getById(int id);
    public boolean updateInfo(U info);
    public boolean getInfo(U info);

}
