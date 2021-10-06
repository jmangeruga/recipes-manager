package my.jmangeruga.recipes.application;

import my.jmangeruga.recipes.domain.Recipe;
import my.jmangeruga.recipes.domain.RecipeRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecipeCrudService {

    private final RecipeRepository recipeRepository;
    private final Clock clock;

    public RecipeCrudService(RecipeRepository recipeRepository, Clock clock) {
        this.recipeRepository = recipeRepository;
        this.clock = clock;
    }

    @Transactional
    public UUID createRecipe(String userId, RecipeCreationRq creationRq) {
        final Recipe newRecipe = creationRq.toDomainModel(userId, clock, recipeRepository);
        final Recipe storedRecipe = recipeRepository.save(newRecipe);
        return storedRecipe.getRecipeId();
    }

    @Transactional(readOnly = true)
    public List<RecipeListEntry> findAll() {
        // This naive first solution might be improved if needed. Some potential problems:
        //  - N + 1 select
        //  - Huge amount of recipes
        return recipeRepository.findAll()
            .stream()
            .map(r ->
                new RecipeListEntry(
                    r,
                    r.getInstructions().stream().map(RecipeListEntry.RecipeInstructionItem::new).collect(Collectors.toList()),
                    r.getIngredients().stream().map(RecipeListEntry.IngredientItem::new).collect(Collectors.toList())
                )
            ).collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteRecipe(String userId, UUID recipeId) {
        verifiedOwnershipRecipe(userId, recipeId);
        final int deletedCount = recipeRepository.deleteByBusinessId(recipeId);
        return deletedCount > 0;
    }

    @Transactional
    public boolean updateRecipe(String userId, UUID recipeId, RecipeModificationRq updatingRq) {
        Optional<Recipe> maybeFoundRecipe = verifiedOwnershipRecipe(userId, recipeId);

        maybeFoundRecipe.ifPresent(updatingRq::applyChanges);
        return maybeFoundRecipe.isPresent();
    }

    private Optional<Recipe> verifiedOwnershipRecipe(String userid, UUID recipeId) {
        Optional<Recipe> maybeFoundRecipe = recipeRepository.findByBusinessId(recipeId);

        maybeFoundRecipe.ifPresent(r -> {
            if (!r.belongsTo(userid)) {
                throw new AccessDeniedException("Current user doesn't own the recipe");
            }
        });

        return maybeFoundRecipe;
    }

}
