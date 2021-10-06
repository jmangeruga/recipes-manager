package my.jmangeruga.recipes.application;

import com.fasterxml.jackson.annotation.JsonCreator;
import my.jmangeruga.recipes.domain.Ingredient;
import my.jmangeruga.recipes.domain.Recipe;
import my.jmangeruga.recipes.domain.RecipeInstruction;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class RecipeModificationRq {

    private final String title;
    private final List<String> instructions;
    private final List<IngredientDetails> ingredients;
    private final Short servings;

    @JsonCreator
    public RecipeModificationRq(
        String title,
        List<String> instructions,
        List<IngredientDetails> ingredients,
        Short servings
    ) {
        this.title = title;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.servings = servings;
    }

    public void applyChanges(Recipe toModifyRecipe) {
        toModifyRecipe.updateRecipeContent(
            title,
            ingredients().orElse(null),
            instructions().orElse(null),
            servings
        );
    }

    private Optional<List<Ingredient>> ingredients() {
        return Optional.ofNullable(ingredients).map(list ->
            list.stream().map(i -> new Ingredient(i.getName(), i.getType())).collect(Collectors.toList())
        );
    }

    private Optional<List<RecipeInstruction>> instructions() {
        return Optional.ofNullable(instructions).map(list ->
            list.stream().map(RecipeInstruction::new).collect(Collectors.toList())
        );
    }

}
