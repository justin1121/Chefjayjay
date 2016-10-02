package ca.circumpunct.chefjayjay.chefjayjay;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Recipe extends RealmObject{

    @PrimaryKey
    private long id;

    private String recipe_name;
    private int duration;
    private RealmList<Ingredient> ingredients;
    private RealmList<CookingStep> cooking_steps;

    private RealmList<RecipeInteractions> interactions;

    public RealmList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(RealmList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public RealmList<CookingStep> getCooking_steps() {
        return cooking_steps;
    }

    public void setCooking_steps(RealmList<CookingStep> cooking_steps) {
        this.cooking_steps = cooking_steps;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public RealmList<RecipeInteractions> getInteractions() {
        return interactions;
    }

    public void setInteractions(RealmList<RecipeInteractions> interactions) {
        this.interactions = interactions;
    }
}
