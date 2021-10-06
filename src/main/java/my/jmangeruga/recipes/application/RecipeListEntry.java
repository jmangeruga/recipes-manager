package my.jmangeruga.recipes.application;

import my.jmangeruga.recipes.domain.Ingredient;
import my.jmangeruga.recipes.domain.Recipe;
import my.jmangeruga.recipes.domain.RecipeInstruction;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static my.jmangeruga.recipes.application.RecipeListEntry.RecipeTag.VEGETARIAN;

final public class RecipeListEntry {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private final Recipe recipe;
    private final List<RecipeInstructionItem> instructions;
    private final List<IngredientItem> ingredients;

    RecipeListEntry(Recipe recipe, List<RecipeInstructionItem> instructions, List<IngredientItem> ingredients) {
        this.recipe = recipe;
        this.instructions = Collections.unmodifiableList(Objects.requireNonNull(instructions));
        this.ingredients = Collections.unmodifiableList(Objects.requireNonNull(ingredients));
    }

    public String getTitle() {
        return recipe.getTitle();
    }

    public Set<RecipeTag> getTags() {
        return recipe.isVegetarian() ? Set.of(VEGETARIAN) : Set.of();
    }

    public UUID getId() {
        return recipe.getRecipeId();
    }

    public List<RecipeInstructionItem> getInstructions() {
        return instructions;
    }

    public List<IngredientItem> getIngredients() {
        return ingredients;
    }

    public String getCreatedAt() {
        return FORMATTER.format(recipe.getCreatedAt().atOffset(ZoneOffset.UTC));
    }

    public Short getServings() {
        return recipe.getServings();
    }

    enum RecipeTag { VEGETARIAN }

    static class IngredientItem {
        private final Ingredient ingredient;

        IngredientItem(Ingredient ingredient) {
            this.ingredient = ingredient;
        }

        public String getName() {
            return ingredient.getName();
        }

        public String getType() {
            return ingredient.getType().name();
        }

    }

    static class RecipeInstructionItem {
        private final RecipeInstruction instruction;

        RecipeInstructionItem(RecipeInstruction instruction) {
            this.instruction = instruction;
        }

        public String getDescription() {
            return instruction.getDescription();
        }
    }

}
