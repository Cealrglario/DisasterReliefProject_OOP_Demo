package edu.ucalgary.oop;

public class SupplyFactory {

    public static Supply createSupply(int supplyId, String type, String comments) {
        Supply supply = null;

        if(type.equalsIgnoreCase("blanket")) {
            supply = new Blanket(supplyId);
        } else if (type.equalsIgnoreCase("cot")) {
            supply = new Cot(supplyId, comments);
        } else if (type.equalsIgnoreCase("water")) {
            supply = new Water(supplyId, false);
        } else if (type.equalsIgnoreCase("personal belonging") || type.equalsIgnoreCase("personal item")) {
            supply = new PersonalBelonging(supplyId, comments);
        } else {
            System.out.println("WARNING: Could not create supply of type '" + type + "'");
            return null;
        }

        if(comments != null) {
            supply.setComments(comments);
        }

        return supply;
    }
}
