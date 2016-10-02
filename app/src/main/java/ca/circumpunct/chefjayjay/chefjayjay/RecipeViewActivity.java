package ca.circumpunct.chefjayjay.chefjayjay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RecipeViewActivity extends AppCompatActivity {
    private Recipe recipe;
    private ArrayList<RecipeInteractions> interactions_added;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        RealmResults<RecipeInteractions> interactions_result;
        interactions_added = new ArrayList<>();

        long recipe_id = getIntent().getLongExtra("recipe_id", 1);

        realm = Realm.getDefaultInstance();

        recipe = realm.where(Recipe.class).equalTo("id", recipe_id).findFirst();

        TextView textView = (TextView) findViewById(R.id.recipe_view_recipe_name);
        textView.setText(recipe.getRecipe_name());

        display_recipe_ingredients();
        display_recipe_cookingsteps();

        interactions_result = recipe.getInteractions().where().findAll();
        interactions_result.addChangeListener(new RealmChangeListener<RealmResults<RecipeInteractions>>() {
            @Override
            public void onChange(RealmResults<RecipeInteractions> element) {
                display_recipe_interactions(element);
            }
        });
        display_recipe_interactions(interactions_result);
    }

    @Override
    protected void onResume(){
        super.onResume();

        RealmResults<RecipeInteractions> interactions_result = recipe.getInteractions().where().findAll();

        display_recipe_interactions(interactions_result);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }

    private void display_recipe_ingredients(){
        RealmList<Ingredient> ingredients = recipe.getIngredients();

        ViewGroup layout = (ViewGroup) findViewById(R.id.layout_recipes_view_ingredients);

        for (final Ingredient ingredient : ingredients) {
            TextView ingredientTextView = new TextView(this);
            ingredientTextView.setTextSize(20);
            ingredientTextView.setText("- " + ingredient.ingredient_name);

            layout.addView(ingredientTextView);
        }
    }

    private void display_recipe_cookingsteps(){
        RealmList<CookingStep> cooking_steps = recipe.getCooking_steps();
        int cookingstep_count = 1;

        ViewGroup layout = (ViewGroup) findViewById(R.id.layout_recipes_view_cooking_steps);

        for (final CookingStep cooking_step : cooking_steps) {
            TextView ingredientTextView = new TextView(this);
            ingredientTextView.setTextSize(20);
            ingredientTextView.setText(cookingstep_count + ". " + cooking_step.desc);

            layout.addView(ingredientTextView);
            cookingstep_count++;
        }
    }

    private void display_recipe_interactions(RealmResults<RecipeInteractions> interactions){
        ViewGroup layout = (ViewGroup) findViewById(R.id.layout_view_interactions);
        int count = 0;

        for(final RecipeInteractions interaction : interactions){
            if(!interactions_added.contains(interaction)){
                Button button = new Button(this);
                button.setText("Interaction " + count);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RecipeViewActivity.this, RecipeInteractActivity.class);
                        intent.putExtra("recipe_id", recipe.getId());
                        intent.putExtra("interaction_id", interaction.getId());
                        startActivity(intent);
                    }

                });
                layout.addView(button);
                count++;
                interactions_added.add(interaction);
            }
        }
    }

    public void add_recipe_interaction(View view){
        Intent intent = new Intent(RecipeViewActivity.this, RecipeInteractActivity.class);
        intent.putExtra("recipe_id", recipe.getId());
        startActivity(intent);
    }
}
