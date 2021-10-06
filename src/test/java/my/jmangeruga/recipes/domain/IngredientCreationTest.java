package my.jmangeruga.recipes.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static my.jmangeruga.recipes.domain.Ingredient.IngredientType.MEAT;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Creation of an ingredient")
class IngredientCreationTest {

    @Test
    @DisplayName("With not blank name and known type should work ok.")
    public void ingredientValidCreation() {
        new Ingredient("Beef", MEAT.name());
    }

    @Test
    @DisplayName("With empty name should throw DomainValidationException.")
    public void ingredientWithEmptyNameIsInvalid() {
        assertThrows(DomainValidationException.class, () -> new Ingredient("", MEAT.name()));
    }

    @Test
    @DisplayName("With null name should throw DomainValidationException.")
    public void ingredientWithNullNameIsInvalid() {
        assertThrows(DomainValidationException.class, () -> new Ingredient(null, MEAT.name()));
    }

    @Test
    @DisplayName("With unknown type should throw IllegalArgumentException.")
    public void ingredientWithUnknownTypeIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new Ingredient("Lamb", "MEATS"));
    }

    @Test
    @DisplayName("With null type should throw DomainValidationException.")
    public void ingredientWithNullTypeIsInvalid() {
        assertThrows(DomainValidationException.class, () -> new Ingredient("Lamb", null));
    }

}