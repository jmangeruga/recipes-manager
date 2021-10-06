package my.jmangeruga.recipes.application;

import my.jmangeruga.recipes.domain.DomainValidationException;
import my.jmangeruga.recipes.domain.Recipe;
import my.jmangeruga.recipes.domain.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("The recipes CRUD service")
class RecipeCrudServiceTest {

    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private Clock clock;
    @InjectMocks
    private RecipeCrudService recipeCrudService;

    @Test
    @DisplayName("When asked to create a recipe and new recipe instance is obtained ok, it should save the recipe and return its business id.")
    public void validRecipeCreation() {
        final Recipe toSaveRecipe = mock(Recipe.class);
        final String executingUserId = "5678";
        final RecipeCreationRq recipeCreationRq = aRecipeCreationRq(executingUserId, toSaveRecipe);

        final UUID storedRecipeId = UUID.fromString("ae6f4e52-d522-47d1-acb7-d36ef7bf3e1e");
        final Recipe storedRecipe = aStoredRecipeWithId(storedRecipeId);
        when(recipeRepository.save(toSaveRecipe)).thenReturn(storedRecipe);

        final UUID actualReturnedRecipeId = recipeCrudService.createRecipe(executingUserId, recipeCreationRq);

        assertThat(actualReturnedRecipeId).isEqualTo(storedRecipeId);
        verifyNoMoreInteractions(recipeRepository);
    }

    private RecipeCreationRq aRecipeCreationRq(String userId, Recipe builtRecipe) {
        final RecipeCreationRq recipeCreationRq = mock(RecipeCreationRq.class);
        when(recipeCreationRq.toDomainModel(userId, clock, recipeRepository)).thenReturn(builtRecipe);
        return recipeCreationRq;
    }

    private Recipe aStoredRecipeWithId(UUID recipeId) {
        final Recipe storedRecipe = mock(Recipe.class);
        when(storedRecipe.getRecipeId()).thenReturn(recipeId);
        return storedRecipe;
    }

    @Test
    @DisplayName("When asked to create a recipe but recipe instantiation fails, it should forward the domain exception without interacting with persistence.")
    public void invalidRecipeCreation() {
        final RecipeCreationRq recipeCreationRq = mock(RecipeCreationRq.class);
        when(recipeCreationRq.toDomainModel(any(), any(), any())).thenThrow(new DomainValidationException(""));

        assertThrows(DomainValidationException.class, () ->
            recipeCrudService.createRecipe("irrelevant_user_id", recipeCreationRq)
        );
        verifyNoInteractions(recipeRepository);
    }

    @Test
    @DisplayName("When asked to retrieve all the recipes, it should return all the available ones.")
    public void allRecipesRetrieval() {
        final List<UUID> storedRecipesId = List.of(
            UUID.fromString("e8460627-4744-47e5-bbb1-cec897cf9ee8"),
            UUID.fromString("92e6463d-425a-4751-aa04-ed266857190a")
        );
        final List<Recipe> storedRecipes =
            storedRecipesId.stream().map(this::recipeWithId).collect(Collectors.toUnmodifiableList());
        when(recipeRepository.findAll()).thenReturn(storedRecipes);

        final List<RecipeListEntry> actualFoundRecipes = recipeCrudService.findAll();

        assertThat(actualFoundRecipes)
            .extracting(RecipeListEntry::getId)
            .containsExactlyInAnyOrderElementsOf(storedRecipesId);
        verifyNoMoreInteractions(recipeRepository);
    }

    private Recipe recipeWithId(UUID id) {
        final Recipe recipe = mock(Recipe.class);
        when(recipe.getRecipeId()).thenReturn(id);
        return recipe;
    }

    @Test
    @DisplayName("When asked to delete an existent recipe owned by given userId, it should remove it and return true.")
    public void existentRecipeDeletion() {
        final UUID recipeIdToDelete = UUID.fromString("387dac96-5996-40a0-8841-ed291d954f52");
        final String executingUserId = "1234";
        storedRecipeThatBelongsTo(recipeIdToDelete, executingUserId);
        when(recipeRepository.deleteByBusinessId(recipeIdToDelete)).thenReturn(1);

        final boolean actualDeletionResult = recipeCrudService.deleteRecipe(executingUserId, recipeIdToDelete);

        assertThat(actualDeletionResult).isTrue();
        verifyNoMoreInteractions(recipeRepository);
    }

    @Test
    @DisplayName("When asked to delete an existent recipe not owned by given userId, it should thrown an AccessDeniedException.")
    public void existentButNotOwnedRecipeDeletion() {
        final UUID recipeIdToDelete = UUID.fromString("66e18777-0058-4d32-9572-c0aac9cbb6e0");
        storedRecipeThatBelongsTo(recipeIdToDelete, "5555");

        final String executingUserId = "1234";
        assertThrows(AccessDeniedException.class, () ->
            recipeCrudService.deleteRecipe(executingUserId, recipeIdToDelete)
        );

        verifyNoMoreInteractions(recipeRepository);
    }

    @Test
    @DisplayName("When asked to delete a nonexistent recipe, it should return a false result.")
    public void nonexistentRecipeDeletion() {
        final UUID recipeIdToDelete = UUID.fromString("b16eb333-ea9e-47db-a4d3-4fef448a7afa");
        when(recipeRepository.findByBusinessId(recipeIdToDelete)).thenReturn(Optional.empty());
        when(recipeRepository.deleteByBusinessId(recipeIdToDelete)).thenReturn(0);

        final boolean actualDeletionResult = recipeCrudService.deleteRecipe("irrelevant_user_id", recipeIdToDelete);

        assertThat(actualDeletionResult).isFalse();
        verifyNoMoreInteractions(recipeRepository);
    }

    @Test
    @DisplayName("When asked to modify an existent recipe owned by given userId and changes are valid, it should return a true result.")
    public void existentRecipeValidModification() {
        final UUID recipeIdToModify = UUID.fromString("66e18777-0058-4d32-9572-c0aac9cbb6e0");
        final String executingUserId = "1234";
        final Recipe foundRecipe = storedRecipeThatBelongsTo(recipeIdToModify, executingUserId);
        final RecipeModificationRq modificationRq = mock(RecipeModificationRq.class);

        final boolean actualModificationResult = recipeCrudService.updateRecipe(executingUserId, recipeIdToModify, modificationRq);

        assertThat(actualModificationResult).isTrue();
        verify(modificationRq).applyChanges(foundRecipe);
        verifyNoMoreInteractions(recipeRepository);
    }

    @Test
    @DisplayName("When asked to modify an existent recipe not owned by given userId, it should thrown an AccessDeniedException.")
    public void existentButNotOwnedRecipeModification() {
        final UUID recipeIdToModify = UUID.fromString("66e18777-0058-4d32-9572-c0aac9cbb6e0");
        storedRecipeThatBelongsTo(recipeIdToModify, "5555");
        final RecipeModificationRq modificationRq = mock(RecipeModificationRq.class);

        final String executingUserId = "1234";
        assertThrows(AccessDeniedException.class, () ->
            recipeCrudService.updateRecipe(executingUserId, recipeIdToModify, modificationRq)
        );

        verifyNoMoreInteractions(recipeRepository);
    }

    @Test
    @DisplayName("When asked to modify a nonexistent recipe, it should return a false result.")
    public void nonexistentRecipeModification() {
        final UUID nonexistentRecipeIdToModify = UUID.fromString("5246e489-8241-4b90-b4a2-6d520ed6133a");
        when(recipeRepository.findByBusinessId(nonexistentRecipeIdToModify)).thenReturn(Optional.empty());
        final RecipeModificationRq modificationRq = mock(RecipeModificationRq.class);

        final boolean actualModificationResult = recipeCrudService.updateRecipe("irrelevant_user_id", nonexistentRecipeIdToModify, modificationRq);

        assertThat(actualModificationResult).isFalse();
        verifyNoInteractions(modificationRq);
        verifyNoMoreInteractions(recipeRepository);
    }

    @Test
    @DisplayName("When asked to modify an existent recipe owned by given userId but changes breaks recipe rules, the exception should be forwarded and no more interactions with persistence should be done.")
    public void existentRecipeInvalidModification() {
        final UUID recipeIdToModify = UUID.fromString("a64040f2-f873-4c37-9a06-ea787e328a40");
        final String executingUserId = "1234";
        final Recipe foundRecipe = storedRecipeThatBelongsTo(recipeIdToModify, executingUserId);

        final RecipeModificationRq recipeCreationRq = mock(RecipeModificationRq.class);
        doThrow(new DomainValidationException("")).when(recipeCreationRq).applyChanges(foundRecipe);

        assertThrows(DomainValidationException.class, () ->
            recipeCrudService.updateRecipe(executingUserId, recipeIdToModify, recipeCreationRq)
        );
        verifyNoMoreInteractions(recipeRepository);
    }

    private Recipe storedRecipeThatBelongsTo(UUID recipeId, String ownerId) {
        final Recipe foundRecipe = mock(Recipe.class);
        lenient().when(foundRecipe.belongsTo(ownerId)).thenReturn(true);
        when(recipeRepository.findByBusinessId(recipeId)).thenReturn(Optional.of(foundRecipe));
        return foundRecipe;
    }

}