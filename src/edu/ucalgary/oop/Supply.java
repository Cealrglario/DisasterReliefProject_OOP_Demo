package edu.ucalgary.oop;

import java.util.Stack;

public abstract class Supply {
    private final int SUPPLY_ID;
    private String type;
    private String comments;

    public Supply(int supplyId, String type) {
        this.SUPPLY_ID = supplyId;
        this.type = type;
    }

    public int getSupplyId() {
        return this.SUPPLY_ID;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
