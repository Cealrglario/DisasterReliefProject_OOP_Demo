package edu.ucalgary.oop;

public class PersonalBelonging extends Supply {

    public PersonalBelonging(int supplyId, String description) {
        super(supplyId, "Personal belonging");
        super.setComments(description);
    }

    @Override
    public void displayDetails() {
        System.out.println(languageManager.getTranslation("personal_belonging_description") + super.getComments());
    }
}
