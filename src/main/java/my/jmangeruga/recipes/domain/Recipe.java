package my.jmangeruga.recipes.domain;

import org.slf4j.LoggerFactory;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static my.jmangeruga.recipes.domain.SimpleDomainValidation.notNull;
import static my.jmangeruga.recipes.domain.SimpleDomainValidation.notNullEmptyList;
import static my.jmangeruga.recipes.domain.SimpleDomainValidation.notNullOrEmpty;
import static my.jmangeruga.recipes.domain.SimpleDomainValidation.validated;
import static my.jmangeruga.recipes.domain.SimpleDomainValidation.withMinValue;

@Entity
public final class Recipe {

    /*
    - Given that it was not specified otherwise, I considered that using only UTC date times was the safest solution. So that, no conversion are needed.
    - Representation of the user id which owns a recipe does not depend on this service. So that, using a plain string instead of a specific.
     */

    private static final Consumer<Instant> NOT_NULL_CREATION = notNull("Creation date is mandatory");
    private static final Consumer<UUID> NOT_NULL_ID = notNull("Recipe business id is mandatory");
    private static final Consumer<Short> NOT_NULL_SERVINGS = notNull("Servings is mandatory");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private UUID businessId;
    private String title;
    @ElementCollection
    @CollectionTable(name = "ingredient")
    private List<Ingredient> ingredients;
    @ElementCollection
    @CollectionTable(name = "instruction")
    private List<RecipeInstruction> instructions;
    private Instant createdAt;
    private String ownerId;
    private Short servings;

    private Recipe() {
    }

    public Recipe(
        UUID businessId,
        String title,
        List<Ingredient> ingredients,
        List<RecipeInstruction> instructions,
        Short servings,
        Instant createdAt,
        String ownerId
    ) {
        this.businessId = validated(NOT_NULL_ID, businessId);
        updateRecipeContent(title, ingredients, instructions, servings);
        this.createdAt = validated(NOT_NULL_CREATION, createdAt);
        this.ownerId = validated(notNullOrEmpty("A non blank owner id is mandatory"), ownerId);
    }

    public void updateRecipeContent(String title, List<Ingredient> ingredients, List<RecipeInstruction> instructions, Short servings) {
        this.title = validated(notNullOrEmpty("A non blank title is mandatory"), title);
        this.ingredients = validated(notNullEmptyList("Ingredients is mandatory and should have 1 or more elements"), ingredients);
        this.instructions = validated(notNullEmptyList("Instructions is mandatory and should have 1 or more elements"), instructions);
        this.servings = validated(
            NOT_NULL_SERVINGS.andThen(withMinValue((short) 1, "Servings should be equal or greater than 1")),
            servings
        );
    }

    public UUID getRecipeId() {
        return businessId;
    }

    public String getTitle() {
        return title;
    }

    public List<Ingredient> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    public List<RecipeInstruction> getInstructions() {
        return Collections.unmodifiableList(instructions);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Short getServings() {
        return servings;
    }

    public boolean isVegetarian() {
        return ingredients.stream().allMatch(Ingredient::isVegetarian);
    }

    public boolean belongsTo(String userId) {
        LoggerFactory.getLogger("Recipe").info("Owner: {} - requester: {}", ownerId, userId);
        return ownerId.equals(userId);
    }

}
