package edu.ucalgary.oop;

public class MainMenu extends Menu {

    public MainMenu(String[] defaultOptions) {
        super(defaultOptions);
        setMinIntInput(1);
        setMaxIntInput(5);
        setRequiresIntInput(true);
    }

    @Override
    public void processInput() {
        switch (intInput) {
            case 1:
                String[] personOptions = languageManager.getMenuTranslation("person_submenu_defaults");
                Menu personMenu = new PersonSubmenu(personOptions);
                personMenu.setCurrentDisplay(personOptions);
                menuManager.navigateToMenu(personMenu);
                break;

            case 2:
                String[] locationOptions = languageManager.getMenuTranslation("location_submenu_defaults");
                LocationSubmenu locationMenu = new LocationSubmenu(locationOptions);
                locationMenu.setCurrentDisplay(locationOptions);
                menuManager.navigateToMenu(locationMenu);
                break;

            case 3:
                String[] inquiryOptions = languageManager.getMenuTranslation("inquiry_submenu_defaults");
                InquirySubmenu inquiryMenu = new InquirySubmenu(inquiryOptions);
                inquiryMenu.setCurrentDisplay(inquiryOptions);
                menuManager.navigateToMenu(inquiryMenu);
                break;

            case 4:
                String[] familyOptions = languageManager.getMenuTranslation("family_group_submenu_defaults");
                Menu familyMenu = new FamilyGroupSubmenu(familyOptions);
                familyMenu.setCurrentDisplay(familyOptions);
                menuManager.navigateToMenu(familyMenu);
                break;

            case 5:
                String[] supplyOptions = languageManager.getMenuTranslation("supply_submenu_defaults");
                Menu supplyMenu = new SupplySubmenu(supplyOptions);
                supplyMenu.setCurrentDisplay(supplyOptions);
                menuManager.navigateToMenu(supplyMenu);
                break;

            default:
                System.out.println(languageManager.getTranslation("error_invalid_option"));
                break;
        }
    }
}
