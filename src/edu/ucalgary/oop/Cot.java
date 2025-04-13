package edu.ucalgary.oop;

public class Cot extends Supply {
    private String cotLocation;

    public Cot(int supplyId, String cotLocation) {
        super(supplyId, "Cot");
        this.cotLocation = cotLocation;
    }

    public String getCotLocation() {
        return cotLocation;
    }

    public boolean setCotLocation(String cotLocation) {
        this.cotLocation = cotLocation;

        return true;
    }

    @Override
    public void displayDetails() {
        System.out.println("Cot location: " + cotLocation);
    }
}
