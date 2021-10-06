package my.jmangeruga.recipes.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static my.jmangeruga.recipes.domain.Ingredient.IngredientType.VEGETABLE;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Creation of a recipe")
final class RecipeCreationTest {

    private final Short two = 2;
    private final List<Ingredient> notEmptyListOfIngredients = List.of(new Ingredient("Tomato", VEGETABLE.name()));
    private final List<RecipeInstruction> notEmptyListOfInstructions = List.of(new RecipeInstruction("Cut tomato"));

    @Test
    @DisplayName("With not null id, not null/empty title, having at least one ingredient and one instruction, servings " +
        "for at least 1 person, not null creation date time and not null should work ok.")
    public void validCreationTest() {
        new Recipe(
            UUID.randomUUID(),
            "Tomato soup",
            notEmptyListOfIngredients,
            notEmptyListOfInstructions,
            two,
            Instant.now(),
            UUID.randomUUID().toString()
        );
    }

    @Test
    @DisplayName("With null business id should throw DomainValidationException.")
    public void recipeWithNullBusinessIdIsInvalid() {
        assertThrows(DomainValidationException.class, () ->
            new Recipe(
                null,
                "Tomato soup",
                notEmptyListOfIngredients,
                notEmptyListOfInstructions,
                two,
                Instant.now(),
                UUID.randomUUID().toString()
            )
        );
    }

    @Test
    @DisplayName("With null title should throw DomainValidationException.")
    public void recipeWithNullTitleIsInvalid() {
        assertThrows(DomainValidationException.class, () ->
            new Recipe(
                UUID.randomUUID(),
                null,
                notEmptyListOfIngredients,
                notEmptyListOfInstructions,
                two,
                Instant.now(),
                UUID.randomUUID().toString()
            )
        );
    }

    @Test
    @DisplayName("With empty title should throw DomainValidationException.")
    public void recipeWithEmptyTitleIsInvalid() {
        assertThrows(DomainValidationException.class, () ->
            new Recipe(
                UUID.randomUUID(),
                "",
                notEmptyListOfIngredients,
                notEmptyListOfInstructions,
                two,
                Instant.now(),
                UUID.randomUUID().toString()
            )
        );
    }

    @Test
    @DisplayName("With empty list of ingredients should throw DomainValidationException.")
    public void recipeWithEmptyListOfIngredientsIsInvalid() {
        final List<Ingredient> emptyListOfIngredients = List.of();
        assertThrows(DomainValidationException.class, () ->
            new Recipe(
                UUID.randomUUID(),
                "Tomato soup",
                emptyListOfIngredients,
                notEmptyListOfInstructions,
                two,
                Instant.now(),
                UUID.randomUUID().toString()
            )
        );
    }

    @Test
    @DisplayName("With null list of ingredients should throw DomainValidationException.")
    public void recipeWithNullListOfIngredientsIsInvalid() {
        assertThrows(DomainValidationException.class, () ->
            new Recipe(
                UUID.randomUUID(),
                "Tomato soup",
                null,
                notEmptyListOfInstructions,
                two,
                Instant.now(),
                UUID.randomUUID().toString()
            )
        );
    }

    @Test
    @DisplayName("With empty list of instructions should throw DomainValidationException.")
    public void recipeWithEmptyListOfInstructionsIsInvalid() {
        final List<RecipeInstruction> emptyListOfInstructions = List.of();
        assertThrows(DomainValidationException.class, () ->
            new Recipe(
                UUID.randomUUID(),
                "Tomato soup",
                notEmptyListOfIngredients,
                emptyListOfInstructions,
                two,
                Instant.now(),
                UUID.randomUUID().toString()
            )
        );
    }

    @Test
    @DisplayName("With null list of instructions should throw DomainValidationException.")
    public void recipeWithNullListOfInstructionsIsInvalid() {
        final List<RecipeInstruction> emptyListOfInstructions = List.of();
        assertThrows(DomainValidationException.class, () ->
            new Recipe(
                UUID.randomUUID(),
                "Tomato soup",
                notEmptyListOfIngredients,
                null,
                two,
                Instant.now(),
                UUID.randomUUID().toString()
            )
        );
    }

    @Test
    @DisplayName("With null servings should throw DomainValidationException.")
    public void recipeWithNullServingsIsInvalid() {
        assertThrows(DomainValidationException.class, () ->
            new Recipe(
                UUID.randomUUID(),
                "Tomato soup",
                notEmptyListOfIngredients,
                notEmptyListOfInstructions,
                null,
                Instant.now(),
                UUID.randomUUID().toString()
            )
        );
    }

    @Test
    @DisplayName("With less than 1 serving should throw DomainValidationException.")
    public void recipeWithLessThanOneServingIsInvalid() {
        final Short zero = 0;
        assertThrows(DomainValidationException.class, () ->
            new Recipe(
                UUID.randomUUID(),
                "Tomato soup",
                notEmptyListOfIngredients,
                notEmptyListOfInstructions,
                zero,
                Instant.now(),
                UUID.randomUUID().toString()
            )
        );
    }

    @Test
    @DisplayName("With null creation date time should throw DomainValidationException.")
    public void recipeWithNullCreationDateIsInvalid() {
        final Short zero = 0;
        assertThrows(DomainValidationException.class, () ->
            new Recipe(
                UUID.randomUUID(),
                "Tomato soup",
                notEmptyListOfIngredients,
                notEmptyListOfInstructions,
                zero,
                null,
                UUID.randomUUID().toString()
            )
        );
    }

    @Test
    @DisplayName("With null owner id should throw DomainValidationException.")
    public void recipeWithNullOwnerIdIsInvalid() {
        final Short zero = 0;
        assertThrows(DomainValidationException.class, () ->
            new Recipe(
                UUID.randomUUID(),
                "Tomato soup",
                notEmptyListOfIngredients,
                notEmptyListOfInstructions,
                zero,
                Instant.now(),
                null
            )
        );
    }

}