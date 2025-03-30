package edu.ucalgary.oop;

public class PersonalBelonging extends Supply {
    private String description;

    public PersonalBelonging(int supplyId, String description) {
        super(supplyId);
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
