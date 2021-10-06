package my.jmangeruga.recipes.domain;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.EnumSet;
import java.util.function.Consumer;

import static my.jmangeruga.recipes.domain.SimpleDomainValidation.notNull;
import static my.jmangeruga.recipes.domain.SimpleDomainValidation.notNullOrEmpty;
import static my.jmangeruga.recipes.domain.SimpleDomainValidation.validated;
import static my.jmangeruga.recipes.domain.SimpleDomainValidation.withMaxSize;

@Embeddable
public class Ingredient {
    private static final Consumer<String> NOT_NULL = notNull("Ingredient type is mandatory");
    private static final Consumer<String> MAX_SIZE = withMaxSize(3000, "Ingredient name max length [3000] exceeded");

    public enum IngredientType {
        MEAT, VEGETABLE, ANIMAL_DERIVED;

        static final EnumSet<IngredientType> vegetarianTypes = EnumSet.of(VEGETABLE, ANIMAL_DERIVED);
    }

    private String name;
    @Enumerated(EnumType.STRING)
    private IngredientType type;

    private Ingredient() {
    }

    public Ingredient(String name, String type) {
        this.name = validated(notNullOrEmpty("A non blank ingredient name is mandatory").andThen(MAX_SIZE), name);
        this.type = IngredientType.valueOf(validated(NOT_NULL, type));
    }

    public String getName() {
        return name;
    }

    public IngredientType getType() {
        return type;
    }

    public boolean isVegetarian() {
        return IngredientType.vegetarianTypes.contains(type);
    }

}
