package edu.ucalgary.oop;

public class Blanket extends Supply {
    public Blanket(int supplyId) {
        super(supplyId, "Blanket");
    }

    @Override
    public void displayDetails() {
        System.out.println("Regular blanket for victim use.");
        if (super.getComments() != null) {
            System.out.println("Comments: " + super.getComments());
        }
    }
}
