package my.jmangeruga.recipes.adapter.db;

import my.jmangeruga.recipes.domain.Recipe;
import my.jmangeruga.recipes.domain.RecipeRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

interface RecipeCrudRepository extends RecipeRepository, CrudRepository<Recipe, Integer> {

    @Override
    Optional<Recipe> findByBusinessId(UUID businessId);

    @Override
    int deleteByBusinessId(UUID businessId);

}
