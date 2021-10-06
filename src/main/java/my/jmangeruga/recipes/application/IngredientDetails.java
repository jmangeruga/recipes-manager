package my.jmangeruga.recipes.application;

public class IngredientDetails {

    private final String name;
    private final String type;

    public IngredientDetails(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

}
