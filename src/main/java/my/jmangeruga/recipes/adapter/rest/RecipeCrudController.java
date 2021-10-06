package my.jmangeruga.recipes.adapter.rest;

import my.jmangeruga.recipes.application.RecipeCreationRq;
import my.jmangeruga.recipes.application.RecipeCrudService;
import my.jmangeruga.recipes.application.RecipeListEntry;
import my.jmangeruga.recipes.application.RecipeModificationRq;
import my.jmangeruga.recipes.adapter.security.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/recipes")
final class RecipeCrudController {

    private static final String RECIPE_ID_PATH_PARAM = "recipe_id";

    private final RecipeCrudService recipeCrudService;

    RecipeCrudController(RecipeCrudService recipeCrudService) {
        this.recipeCrudService = recipeCrudService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleResourceCreatedRs> createRecipe(@RequestBody RecipeCreationRq creationRq, @AuthenticationPrincipal User authenticatedUser) {
        final UUID createdRecipeId = recipeCrudService.createRecipe(authenticatedUser.getId(), creationRq);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SingleResourceCreatedRs(createdRecipeId.toString()));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<MultipleResultsRs<RecipeListEntry>> listAllRecipes() {
        return ResponseEntity.ok(new MultipleResultsRs<>(recipeCrudService.findAll()));
    }

    @DeleteMapping(path = "/{" + RECIPE_ID_PATH_PARAM + "}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleMessageRs> deleteRecipe(@PathVariable(RECIPE_ID_PATH_PARAM) String recipeId, @AuthenticationPrincipal User authenticatedUser) {
        final boolean successfulDeletion = recipeCrudService.deleteRecipe(authenticatedUser.getId(), UUID.fromString(recipeId));

        if (successfulDeletion) {
            return ResponseEntity.ok(new SingleMessageRs("Recipe deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SingleMessageRs("Recipe not found."));
        }
    }

    @PutMapping(path = "/{" + RECIPE_ID_PATH_PARAM + "}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleMessageRs> updateRecipe(
        @PathVariable(RECIPE_ID_PATH_PARAM) String recipeId,
        @RequestBody RecipeModificationRq recipeModificationRq,
        @AuthenticationPrincipal User authenticatedUser
    ) {
        final boolean successfulModification =
            recipeCrudService.updateRecipe(authenticatedUser.getId(), UUID.fromString(recipeId), recipeModificationRq);

        if (successfulModification) {
            return ResponseEntity.ok(new SingleMessageRs("Recipe updated successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SingleMessageRs("Recipe not found."));
        }
    }

}
