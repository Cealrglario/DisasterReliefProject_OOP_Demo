package edu.ucalgary.oop;

public class Cot extends Supply {
    public Cot(int supplyId, String cotLocation) {
        super(supplyId, "Cot");
        super.setComments(cotLocation);
    }

    @Override
    public void displayDetails() {
        System.out.println("Cot location: " + super.getComments());
    }
}
