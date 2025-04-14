package edu.ucalgary.oop;

public class Blanket extends Supply {
    public Blanket(int supplyId) {
        super(supplyId, "Blanket");
    }

    @Override
    public void displayDetails() {
        System.out.println(languageManager.getTranslation("blanket_description"));

        if (super.getComments() != null) {
            System.out.println(languageManager.getTranslation("comments") + ": " + super.getComments());
        }
    }
}
