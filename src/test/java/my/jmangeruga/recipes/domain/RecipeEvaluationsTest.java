package my.jmangeruga.recipes.domain;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static my.jmangeruga.recipes.domain.Ingredient.IngredientType.MEAT;
import static my.jmangeruga.recipes.domain.Ingredient.IngredientType.VEGETABLE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("A created recipe")
final class RecipeEvaluationsTest {

    private final Short two = 2;
    private final List<Ingredient> notEmptyListOfIngredients = List.of(new Ingredient("Tomato", VEGETABLE.name()));
    private final List<RecipeInstruction> notEmptyListOfInstructions = List.of(new RecipeInstruction("Cut tomato"));

    @Test
    @DisplayName("When asked whether belongs to the user id used during creation should return true.")
    public void recipeBelongsToTheUserIdUsedDuringCreationShouldBeTrue() {
        final String ownerId = UUID.randomUUID().toString();
        final Recipe aRecipe = aRecipeThatBelongsTo(ownerId);

        final boolean actualOwnershipEvaluation = aRecipe.belongsTo(ownerId);

        assertThat(actualOwnershipEvaluation)
            .as(new TextDescription("Belongs to owner"))
            .isTrue();
    }

    @Test
    @DisplayName("When asked whether belongs to and user id different from the used during creation should return false.")
    public void recipeBelongsToAnUserIdDifferentFromTheUsedDuringCreationShouldBeFalse() {
        final String ownerId = UUID.randomUUID().toString();
        final Recipe aRecipe = aRecipeThatBelongsTo(ownerId);

        final String notTheOwnerId = UUID.randomUUID().toString();
        final boolean actualOwnershipEvaluation = aRecipe.belongsTo(notTheOwnerId);

        assertThat(actualOwnershipEvaluation)
            .as(new TextDescription("Belongs to owner"))
            .isFalse();
    }

    private Recipe aRecipeThatBelongsTo(String ownerId) {
        return new Recipe(
            UUID.randomUUID(),
            "Tomato soup",
            notEmptyListOfIngredients,
            notEmptyListOfInstructions,
            two,
            Instant.now(),
            ownerId
        );
    }

    @Test
    @DisplayName("Built up only with vegetarian ingredients, when asked if it's vegetarian should return true.")
    public void recipeBuiltWithOnlyVegetarianIngredientsShouldBeVegetarian() {
        final Recipe aRecipeWithVegetarianIngredients = new Recipe(
            UUID.randomUUID(),
            "Tomato soup",
            List.of(new Ingredient("Potatoes", VEGETABLE.name()), new Ingredient("Tomatoes", VEGETABLE.name())),
            notEmptyListOfInstructions,
            two,
            Instant.now(),
            UUID.randomUUID().toString()
        );

        final boolean actualVegetarianismEvaluation = aRecipeWithVegetarianIngredients.isVegetarian();

        assertThat(actualVegetarianismEvaluation)
            .as(new TextDescription("Vegetarian recipe"))
            .isTrue();
    }

    @Test
    @DisplayName("Built up not only with vegetarian ingredients, when asked if it's vegetarian should return false.")
    public void recipeBuiltWithNotOnlyVegetarianIngredientsShouldNotBeVegetarian() {
        final Recipe aRecipeWithVegetarianIngredients = new Recipe(
            UUID.randomUUID(),
            "Tomato soup",
            List.of(new Ingredient("Potatoes", VEGETABLE.name()), new Ingredient("Pork", MEAT.name())),
            notEmptyListOfInstructions,
            two,
            Instant.now(),
            UUID.randomUUID().toString()
        );

        final boolean actualVegetarianismEvaluation = aRecipeWithVegetarianIngredients.isVegetarian();

        assertThat(actualVegetarianismEvaluation)
            .as(new TextDescription("Vegetarian recipe"))
            .isFalse();
    }
}