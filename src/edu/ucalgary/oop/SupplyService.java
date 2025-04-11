package edu.ucalgary.oop;

import java.sql.SQLException;
import java.util.List;

public enum SupplyService {
    INSTANCE;

    private final SupplyAccess<Object> supplyAccess = new SupplyAccess<>();

    public Supply getSupplyById(int supplyId) throws SQLException {
        return supplyAccess.getById(supplyId);
    }

    public List<Supply> getAllSupplies() throws SQLException {
        return supplyAccess.getAll();
    }

    public Supply addSupply(String type, String comments) throws SQLException {
        return supplyAccess.addSupply(type, comments);
    }

    public boolean updateSupplyComments(Supply supply, String newComments) throws SQLException {
        supply.setComments(newComments);
        return supplyAccess.updateInfo("comments", newComments, supply.getSupplyId());
    }

    public boolean removeSupply(Supply supply) throws SQLException {
        return supplyAccess.removeSupply(supply);
    }
}
