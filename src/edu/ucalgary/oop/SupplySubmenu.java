package edu.ucalgary.oop;

public class SupplySubmenu extends Menu {
    private String[] MANAGE_SUPPLY_OPTIONS;

    public SupplySubmenu(String[] defaultOptions) {
        super(defaultOptions);
    }

    public String[] getManageSupplyOptions() {
        return this.MANAGE_SUPPLY_OPTIONS;
    }

    public void viewSupplyInventory() {}

    public void viewSupplyComments() {}

    public void manageSupplyDetails() {}

    public void giveSupplyToLocation() {}

    public void checkWaterExpiration() {}

    @Override
    public void processInput() {}
}

