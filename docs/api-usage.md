# REST API
With the default provided configuration, all request should be directed to:
```
https://localhost:8443
```

## Authentication
All the requests must provide a valid authentication token within `Authorization` header.

Example:
```
Authorization: Bearer AAAABBBBCCCC
```

Check the valid tokens per environment:  
[dev](../src/main/resources/application-dev.yml)  
[docker-compose](../docker/docker-compose.yml)

### Authentication problems
Whenever a token is not valid, the general response will be:
- 401 Unauthorized
```json
{
    "message": "Unauthorized"
}
```

## Create recipe
Creation of a new recipe

`POST /api/v1/recipes`

### Request body
```json
{
  "title": "A recipe",
  "ingredients": [
    {
      "name": "rice",
      "type": "VEGETABLE"
    },
    {
      "name": "tomato",
      "type": "VEGETABLE"
    }
  ],
  "instructions": [
    "Instruction 1",
    "Instruction 2",
    "Instruction 3"
  ],
  "servings": 1
}
```
- Valid ingredient types: `VEGETAL`, `MEAT`, `ANIMAL_DERIVED`

### Responses
- 201 Created  
```json
{
    "id": "d6a7d607-6c80-41c7-a2dc-2b02f04b6950"
}
```
- 400 Bad request
```json
{
    "message": "Incorrect input",
    "description": "A non blank title is mandatory"
}
```

## Retrieve recipes
Retrieval of all the possible recipes

`GET /api/v1/recipes`

### Responses
- 200 OK
```json
{
  "items": [
    {
      "instructions": [
        {
          "description": "Instruction 1"
        },
        {
          "description": "Instruction 2"
        },
        {
          "description": "Instruction 3"
        }
      ],
      "ingredients": [
        {
          "name": "100g rice",
          "type": "VEGETABLE"
        },
        {
          "name": "1 tomato",
          "type": "VEGETABLE"
        }
      ],
      "tags": [
        "VEGETARIAN"
      ],
      "id": "412b65c1-567f-4439-9ef6-ff8bb4d7e3f2",
      "title": "A recipe",
      "created_at": "06-10-2021 00:24",
      "servings": 1
    }
  ]
}
```
- `tags` at the moment can only be `VEGETARIAN` when all the ingredients of the recipe meet the characteristic of not being `MEAT`.

## Remove recipe
Deletion of a given recipe

`DELETE /api/v1/recipes/{recipe_id}`

Where `recipe_id` is the id gotten after successfully creating a recipe.

### Responses
- 200
```json
{
    "message": "Recipe deleted successfully."
}
```
- 404 Not found
```json
{
    "message": "Recipe not found."
}
```
## Update recipe
Full modification of a given recipe

`PUT /api/v1/recipes/{recipe_id}`

Where `recipe_id` is the id gotten after successfully creating a recipe.

### Request body

```json
{
  "title": "A recipe",
  "ingredients": [
    {
      "name": "rice",
      "type": "VEGETABLE"
    },
    {
      "name": "tomato",
      "type": "VEGETABLE"
    }
  ],
  "instructions": [
    "Instruction 1",
    "Instruction 2",
    "Instruction 3"
  ],
  "servings": 1
}
```

### Responses
- 200
```json
{
    "message": "Recipe updated successfully."
}
```
- 404 Not found
```json
{
    "message": "Recipe not found."
}
```