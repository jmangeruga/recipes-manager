package my.jmangeruga.recipes.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Creation of a recipe instruction")
final class RecipeInstructionCreationTest {

    @Test
    @DisplayName("With not blank name should work ok.")
    public void recipeInstructionValidCreation() {
        new RecipeInstruction("Boil the potatoes for 25 min.");
    }

    @Test
    @DisplayName("With empty name should throw DomainValidationException.")
    public void recipeInstructionWithEmptyDescriptionIsInvalid() {
        Assertions.assertThrows(DomainValidationException.class, () -> new RecipeInstruction(""));
    }

    @Test
    @DisplayName("With null name should throw DomainValidationException.")
    public void recipeInstructionWithNullDescriptionIsInvalid() {
        Assertions.assertThrows(DomainValidationException.class, () -> new RecipeInstruction(null));
    }

}