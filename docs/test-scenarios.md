# Test scenarios

## Scenario 1: Recipe successful creation

**Given** a user is correctly authenticated  
**When** the user sends a POST request to create a recipe with correct data  
**Then** a recipe associated to the user is created
And the user will get a successful response with the id of the created recipe

## Scenario 2: Recipe successful listing

**Given** the system has two previously created recipes  
And a user is correctly authenticated  
**When** the user sends a GET request to list all the recipes  
**Then** the user will get a successful response with the list of recipes

## Scenario 3: Delete nonexistent recipe

**Given** the system has only one recipe with id e0321008-9a63-4234-9272-fcd963eff23a  
And a user is correctly authenticated  
**When** the user sends a DELETE request for recipe 53e16ed4-ce24-4357-91bc-6242889d7ad0  
**Then** nothing happens in the system  
And the user gets a not found response

## Scenario 4: Delete existent owned recipe

**Given** the system has a recipe with id e0321008-9a63-4234-9272-fcd963eff23a  
And a user is correctly authenticated  
And the recipe belongs to the authenticated user  
**When** the user sends a DELETE request for recipe e0321008-9a63-4234-9272-fcd963eff23a  
**Then** the recipe is removed from the system  
And the user gets a not found response

## Scenario 5: Failed recipe creation due to missing field

**Given** a user is correctly authenticated  
**When** the user sends a POST request to create a recipe without ingredients    
**Then** the recipe is not created  
And the user gets a bad request response indicating the missing data

## Scenario 6: Failed recipe creation due to invalid data

**Given** a user is correctly authenticated  
**When** the user sends a POST request to create a recipe with an empty title   
**Then** the recipe is not created  
And the user gets a bad request response indicating the problem

## Scenario 6: Unauthenticated request forbidden

**Given** an unauthenticated user  
**When** the user sends a POST request to create a recipe with correct data   
**Then** the recipe is not created  
And the user gets an unauthenticated response

## Scenario 7: Update existent not owned recipe

**Given** the system has a recipe with id e0321008-9a63-4234-9272-fcd963eff23a  
And the recipe belongs to user 1e6c1cbb-69be-42b9-89ad-e8a2aca001d3  
And the user 10398b59-e311-4c5e-82b3-d9bd76f96350 is correctly authenticated   
**When** the authenticated user sends a valid PUT request for recipe e0321008-9a63-4234-9272-fcd963eff23a  
**Then** the recipe is not altered  
And the user gets an unauthorized response

## Scenario 8: Failed recipe modification due to missing field

**Given** a user is correctly authenticated  
**When** the user sends a PUT request to modify a recipe without instructions  
**Then** the recipe is not modified  
And the user gets a bad request response indicating the problem

## Scenario 9: Failed recipe modification due to invalid data

**Given** a user is correctly authenticated  
**When** the user sends a PUT request to modify a recipe with an empty title   
**Then** the recipe is not modified  
And the user gets a bad request response indicating the problem

## Scenario 10: Modify nonexistent recipe

**Given** the system has only one recipe with id e0321008-9a63-4234-9272-fcd963eff23a  
And a user is correctly authenticated  
**When** the user sends a correct PUT request to modify recipe 53e16ed4-ce24-4357-91bc-6242889d7ad0  
**Then** nothing happens in the system  
And the user gets a not found response

## Implementation of tests
An implementation for these scenarios was not provided. Next, I give some ideas that might be used for implementing them.

I see the scenarios as good examples to use a `black-box` test approach in which we exercise each case against the REST API, ensuring that all the components work together.

The application needs to be started up as a whole, but without depending on a real database. Instead, an in-memory custom implementation of the `RecipeRepository` could be injected, for which `spring` provide testing facilities.

Assertions would be done against the same REST API when possible. I would allow assertions against the in-memory repository if needed. Though, depending on the taste, another variation would be to provide an extended REST API for testing purposes.