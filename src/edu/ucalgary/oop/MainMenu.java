package edu.ucalgary.oop;

public class MainMenu extends Menu {

    public MainMenu(String[] defaultOptions) {
        super(defaultOptions);
    }

    @Override
    public void processInput() {
        switch (intInput) {
            case 1: // Manage Persons
                PersonSubmenu personSubmenu = new PersonSubmenu(
                        languageManager.getMenuTranslation("person_submenu_defaults"));
                menuManager.navigateToMenu(personSubmenu);
                break;

            case 2: // Manage Locations
                LocationSubmenu locationSubmenu = new LocationSubmenu(
                        languageManager.getMenuTranslation("location_submenu_defaults"));
                menuManager.navigateToMenu(locationSubmenu);
                break;

            case 3: // Manage Inquiries
                InquirySubmenu inquirySubmenu = new InquirySubmenu(
                        languageManager.getMenuTranslation("inquiry_submenu_defaults"));
                menuManager.navigateToMenu(inquirySubmenu);
                break;

            default:
                System.out.println(languageManager.getTranslation("error_invalid_option"));
                break;
        }
    }
}
