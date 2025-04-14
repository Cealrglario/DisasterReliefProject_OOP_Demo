package edu.ucalgary.oop;

public class SupplyFactory {
    private static final LanguageManager languageManager = LanguageManager.INSTANCE;

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
            System.out.println(languageManager.getTranslation("warning_supply_type_creation") + "'" + type + "'");
            return null;
        }

        if(comments != null) {
            supply.setComments(comments);
        }

        return supply;
    }
}
