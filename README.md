# Chefjayjay

Chefjayjay is a prototype app which allows you to share your recipes and share your adventures making them! Currently all data is stored locally in a Realm database. For each recipe added there are groups of ingredients and cooking steps. Once a recipe is created interactions can be added to the recipe. Currently only adding pictures from your local phone gallery is supported.

# App Activities

This app contains four activities:

- MainActivity, landing page where the user can add new recipes and view already added recipes
- CreateNewRecipeActivity, the place to create new recipes, add a name, ingredients and cooking steps
- RecipeViewActivity, view the added recipe
- RecipeInteractActivity, add pictures to an interaction with a recipe

# Realm Database Schema

The realm database schema consists of five objects:

- Recipe
  - id, priamry key of the recipe
  - duration, suggested duration of recipe currently not in use
  - ingredients, one to many relationship to Ingredient
  - cooking_steps, one to many relationship to CookingStep
  - interactions, one to many relationship to RecipeInteractions
- Ingredient
  - ingredient_name, name/description of the ingredient
- CookingStep
  - desc, description of the cooking step
- RecipeInteractions
  - id, primary key of the interaction
  - images, one to many relationship to ImageLocator
- ImageLocator
  - image_uri, the images uniform resource identifier 

# Future Work

- support other types of media (i.e. videos, gifs and text)
- allow user to take pictures from within app
- store data in the cloud in a postgresql database
- support creation of users and sharing to other users directly
- make the app look better
- allow ratings for the recipes and the interactions
- import recipes from other sources
