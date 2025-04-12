package edu.ucalgary.oop;

public abstract class Supply {
    private final int SUPPLY_ID;
    private final String TYPE;
    private String comments;

    public Supply(int supplyId, String type) {
        this.SUPPLY_ID = supplyId;
        this.TYPE = type;
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
        return TYPE;
    }
}
