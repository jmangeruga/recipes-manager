package my.jmangeruga.recipes.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository extends RecipeIdentityProvider {

    Recipe save(Recipe recipe);

    List<Recipe> findAll();

    Optional<Recipe> findByBusinessId(UUID businessId);

    int deleteByBusinessId(UUID businessId);

}
